# groovy-boot (prologue)
This project documents a history of failed attempts to create a Spring Boot 2 application capable
of dynamically generating components ready to be scanned and incorporated into Spring's application context.
Origins of this feat go back to innocently looking [Stack Overflow question](https://stackoverflow.com/questions/61469366/how-to-supply-runtime-generated-groovy-classes-to-spring-boot-context-configurat).

I dedicate this to [Ellen Ullman](https://en.wikipedia.org/wiki/Ellen_Ullman), for reassuring me that it is ok to have small islands of knowledge and enormous chasms between them.

# stuff tried so far
* Associate `GroovyClassLoader` with Spring Boot startup sequence.
* Create custom in-memory URL implementation to serve `URLClassLoader` for classes generated in runtime.
* Try to serve classes from a virtual in-memory file system using [Jimfs](https://github.com/google/jimfs).
* Use a couple of bytecode engineering libs ([asm](https://asm.ow2.io/), [BCEL](https://commons.apache.org/proper/commons-bcel/)) to dump a class file to a byte array.
* Use `URLClassLoader` to reference existing precompiled Groovy classes.
* Compile and dump groovy classes on the fly, only to be picked by `URLClassLoader` which makes them visible to Spring Boots component scanning mechanism.

# alternative
* Use `SpringApplicationBuilder.sources(Class<?>...)` to register custom components to Spring Boot (as described in [an answer](https://stackoverflow.com/a/61566251/2400849) to the previously mentioned question).

# license
This work is released under [wtfpl2 license](https://wtfpl2.com/).