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