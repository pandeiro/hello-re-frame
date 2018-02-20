# Hello re-frame

## 01 - Beginnings

At this point, our app is set up with just the beginnings of what we want:

- Dependencies for the app and development are defined in `boot.build` and `boot.properties`
- An initial HTML page is defined in `resources/index.html`
- A React-based ClojureScript app that simply renders "Hello World" is defined in `src/hello/core.cljs`
- Some development niceties are present: changes to source code reload automatically (boot-reload), the page is served over HTTP (boot-http), and ClojureScript data structures can be logged to the browser's dev console (cljs-devtools)
