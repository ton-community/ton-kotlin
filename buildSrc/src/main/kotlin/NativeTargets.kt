import org.gradle.api.Project
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.getting
import org.gradle.kotlin.dsl.provideDelegate
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.AbstractKotlinNativeTargetPreset
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.konan.target.HostManager

/**
 * Specifies what subset of Native targets to build
 *
 * ALL — all possible for compilation
 * HOST — host-specific ones, without cross compilation (e.g. do not build linuxX64 on macOS host)
 * SINGLE — only for current OS (to import into IDEA / quickly run tests)
 * DISABLED — disable all Native targets (useful with Kotlin compiler built from sources)
 *
 * For HOST mode, all targets are still listed in .module file, so HOST mode is useful for release process.
 */
enum class NativeState {
    ALL, HOST, SINGLE, DISABLED
}

fun getNativeState(description: String?): NativeState {
    if (description == null) return NativeState.SINGLE
    return when (description.toLowerCase()) {
        "all", "true" -> NativeState.ALL
        "host" -> NativeState.HOST
        "single" -> NativeState.SINGLE
        "disabled" -> NativeState.DISABLED
        else -> NativeState.SINGLE
    }
}

val Project.ideaActive: Boolean
    get() = System.getProperty("idea.active")?.toBoolean() ?: false

val Project.projectNativeState: NativeState
    get() = getNativeState(findProperty("native.state").toString())

val Project.singleTargetMode get() = ideaActive || projectNativeState == NativeState.SINGLE

fun Project.nativeTargets(nativeState: NativeState = projectNativeState, native: NativeExtension.() -> Unit = {}) {
    println("Native state: $nativeState")
    if (nativeState == NativeState.DISABLED) return
    val kotlin = extensions.findByType<KotlinMultiplatformExtension>() ?: return
    val target = if (nativeState == NativeState.SINGLE) {
        IdeaNativeExtension(this, kotlin, "native")
    } else {
        BuildNativeExtension(this, kotlin, "native")
    }
    target.native()
    val commonMain = kotlin.sourceSets.getByName("commonMain")
    val nativeMain = kotlin.sourceSets.getByName("nativeMain")
    nativeMain.dependsOn(commonMain)
}

abstract class NativeExtension(
    val project: Project,
    val kotlin: KotlinMultiplatformExtension,
    val sourceSetName: String
) {
    val mainSourceSet = kotlin.sourceSets.maybeCreate("${sourceSetName}Main")
    val testSourceSet = kotlin.sourceSets.maybeCreate("${sourceSetName}Test")

    abstract fun target(name: String, configure: NativeExtension.(target: KotlinNativeTarget) -> Unit = {})
    abstract fun common(name: String, configure: NativeExtension.() -> Unit = {})
}

class IdeaNativeExtension(
    project: Project,
    kotlin: KotlinMultiplatformExtension,
    sourceSetName: String
) : NativeExtension(project, kotlin, sourceSetName) {
    private val hostManager = HostManager()
    private val hostTarget = HostManager.host
    private val hostPreset =
        kotlin.presets.filterIsInstance<AbstractKotlinNativeTargetPreset<*>>().let { nativePresets ->
            nativePresets.singleOrNull { preset ->
                hostManager.isEnabled(preset.konanTarget) && hostTarget == preset.konanTarget
            }
                ?: error("No native preset of ${nativePresets.map { it.konanTarget }} matches current host target $hostTarget")
        }
    private val registeredTargets = ArrayList<KotlinNativeTarget>()

    override fun target(name: String, configure: NativeExtension.(target: KotlinNativeTarget) -> Unit) {
        if (name != hostPreset.name) return
        val target = kotlin.targetFromPreset(hostPreset, sourceSetName) {
            configure(this)
        }
        registeredTargets.add(target)
        mainSourceSet.kotlin.srcDir("${hostPreset.name}Main/src")
    }

    override fun common(name: String, configure: NativeExtension.() -> Unit) {
        val extension = IdeaNativeExtension(project, kotlin, name)
        extension.mainSourceSet.dependsOn(mainSourceSet)
        extension.testSourceSet.dependsOn(testSourceSet)
        extension.configure()
        if (extension.registeredTargets.isEmpty()) {
            println("Unregistering common source set: $name")
            extension.kotlin.sourceSets.remove(extension.mainSourceSet)
            extension.kotlin.sourceSets.remove(extension.testSourceSet)
        }
    }
}

class BuildNativeExtension(
    project: Project,
    kotlin: KotlinMultiplatformExtension,
    sourceSetName: String
) : NativeExtension(project, kotlin, sourceSetName) {
    private val nativePresets = kotlin.presets.filterIsInstance<AbstractKotlinNativeTargetPreset<*>>()

    override fun target(name: String, configure: NativeExtension.(target: KotlinNativeTarget) -> Unit) {
        val preset = nativePresets.singleOrNull { it.name == name } ?: return

        val target = kotlin.targetFromPreset(preset) {
            configure(this)
        }

        kotlin.sourceSets.getByName("${preset.name}Main").dependsOn(mainSourceSet)
        kotlin.sourceSets.getByName("${preset.name}Test").dependsOn(testSourceSet)
    }

    override fun common(name: String, configure: NativeExtension.() -> Unit) {
        kotlin.sourceSets.create("${name}Main").dependsOn(mainSourceSet)
        kotlin.sourceSets.create("${name}Test").dependsOn(testSourceSet)
        val extension = BuildNativeExtension(project, kotlin, name)
        extension.configure()
    }
}
