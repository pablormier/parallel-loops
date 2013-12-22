parallel-loops [![Build Status](https://travis-ci.org/pablormier/parallel-loops.png)](https://travis-ci.org/pablormier/parallel-loops) [![Coverage Status](https://coveralls.io/repos/pablormier/parallel-loops/badge.png?branch=master)](https://coveralls.io/r/pablormier/parallel-loops?branch=master)
==============

Very simple & lightweight util class for Java to assist the creation of concurrent tasks and loop parallelization. You can use this library to process collections in parallel just using very few lines. If you need something more powerful, I recommend you to check the functionaljava project which provides high-order concurrency abstractions http://code.google.com/p/functionaljava/.

## Example

```java
Collection<String> upperCaseWords = 
        Parallel.ForEach(words, new Parallel.F<String, String>() {
            public String apply(String s) {
                return s.toUpperCase();
            }
        });
```

## Usage

If you use Maven, just paste the following snippet into your pom.xml:

```xml
<dependency>
    <groupId>es.usc.citius.common</groupId>
    <artifactId>parallel-loops</artifactId>
    <version>1.0</version>
</dependency>
```
