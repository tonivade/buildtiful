# buildtiful
Building tool of my dreams. IMHO existing tools like maven, gradle or sbt are to complex.

```yaml
  project:
    groupId: com.github.tonivade
    artifactId: test
    version: 0.1.0-SNAPSHOT

  sources:
    main:
      - 'src/main/java'"
      - 'src/main/resources'"
    test:
      - 'src/test/java'"
      - 'src/test/resources'"

  plugins:
    - java

  repositories:
    - id: ossrh
      url: 'https://oss.sonatype.org/content/repositories/snapshots'

  dependencies:
    compile:
      - 'com.github.tonivade:resp-server:0.6.0'
    test:
      - 'junit:junit:4.12'

  build:
    - clean
    - compile
    - test
```
