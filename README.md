# buildtiful
Building tool of my dreams

```
  project:
    groupId: com.github.tonivade
    artifactId: test
    version: 0.1.0-SNAPSHOT

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
