package ss.wallpad.testing

@Target(AnnotationTarget.ANNOTATION_CLASS)
annotation class OpenClass

@OpenClass
@Target(AnnotationTarget.CLASS)
annotation class OpenForTesting
