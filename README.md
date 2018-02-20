# Hello re-frame

An example webapp that allows for searching GitHub for
re-frame-related code samples, designed to show usage of the re-frame
UI framework.

---

(The following sections correspond to git tags that can be visited to observe the state
of the app under development at that time.)

## 01 - Beginnings

At this point, our app is set up with just the beginnings of what we want:

- Dependencies for the app and development are defined in `boot.build` and `boot.properties`
- An initial HTML page is defined in `resources/index.html`
- A React-based ClojureScript app that simply renders "Hello World" is defined in `src/hello/core.cljs`
- Some development niceties are present: changes to source code reload automatically (boot-reload), the page is served over HTTP (boot-http), and ClojureScript data structures can be logged to the browser's dev console (cljs-devtools)

## 02 - Basic Search

The first prototype of the app:

- A search form and area for results
- A network request to the GitHub code search API that uses the query from our form
- An additional request to retrieve the code from the files mentioned in the search results

The code works but is all thrown together in 80-something lines within
`src/hello/core.cljs`, where presentation logic, data retrieval,
events processing and utility functions are intertwined. Application
state is managed via top-level vars that hold configuration
information and two reagent atoms that store form state and search
results.

## 03 - Refactor using re-frame

A re-thinking of how to organize the app into its structural components:

- Events
- State
- Presentation

All user activity flows through an event processing framework in
`src/hello/events.cljs`. State is managed by the return values of
these events' handler functions, and retrieved via a central "db".

Cross-cutting concerns such as authentication credentials are
explicitly initialized at app init.

Presentation logic has been split into `src/hello/views.cljs`, and
accesses the app's state via subscriptions in `src/hello/subs.cljs`.
