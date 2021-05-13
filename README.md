# WebFlux Sample
## Concurrency Model
Both Spring MVC and Spring WebFlux support annotated controllers, but there is a key difference in the concurrency model and the default assumptions for blocking and threads.

In Spring MVC (and servlet applications in general), it is assumed that applications can block the current thread, (for example, for remote calls). For this reason, servlet containers use a large thread pool to absorb potential blocking during request handling.

In Spring WebFlux (and non-blocking servers in general), it is assumed that applications do not block. Therefore, non-blocking servers use a small, fixed-size thread pool (event loop workers) to handle requests.

```
“To scale” and “small number of threads” may sound contradictory but to never block the 
current thread (and rely on callbacks instead) means that you do not need extra threads, 
as there are no blocking calls to absorb.
```

&copy; [hongxi.org](http://hongxi.org) | [web.hongxi.org](http://web.hongxi.org)