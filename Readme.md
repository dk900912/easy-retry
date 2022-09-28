<p align="center">
<a href="https://openjdk.java.net/"><img src="https://img.shields.io/badge/Java-8+-green?logo=java&logoColor=white" alt="Java support"></a>
<a href="https://www.apache.org/licenses/LICENSE-2.0.html"><img src="https://img.shields.io/github/license/dk900912/easy-retry?color=4D7A97&logo=apache" alt="License"></a>
<a href="https://search.maven.org/search?q=a:easy-retry"><img src="https://img.shields.io/maven-central/v/io.github.dk900912/easy-retry?logo=apache-maven" alt="Maven Central"></a>
<a href="https://github.com/dk900912/easy-retry/releases"><img src="https://img.shields.io/github/release/dk900912/easy-retry.svg" alt="GitHub release"></a>
<a href="https://github.com/dk900912/easy-retry/stargazers"><img src="https://img.shields.io/github/stars/dk900912/easy-retry" alt="GitHub Stars"></a>
<a href="https://github.com/dk900912/easy-retry/fork"><img src="https://img.shields.io/github/forks/dk900912/easy-retry" alt="GitHub Forks"></a>
<a href="https://github.com/dk900912/easy-retry/issues"><img src="https://img.shields.io/github/issues/dk900912/easy-retry" alt="GitHub issues"></a>
<a href="https://github.com/dk900912/easy-retry/graphs/contributors"><img src="https://img.shields.io/github/contributors/dk900912/easy-retry" alt="GitHub Contributors"></a>
<a href="https://github.com/dk900912/easy-retry"><img src="https://img.shields.io/github/repo-size/dk900912/easy-retry" alt="GitHub repo size"></a>
</p>

### How to use
```java
RetryTemplate retryTemplate = RetryTemplate.builder()
        .maxAttempts(6)
        .exponentialBackoff(1000, 2, 10000)
        .retryOn(IllegalArgumentException.class)
        .build();
retryTemplate.execute(callback);
```
### You are welcome to enjoy it