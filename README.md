# News

Minimal news app for android using MVP/Dagger2/RxJava2.

This app uses [NewsApi](https://newsapi.org/) for fetching news.

## Build Instructions

1. Clone this project.
2. Create config.gradle in project root directory with these content:

```
ext.config = [
        apiKey:'\"YOUR_API_KEY_HERE\"'
]
```

3. Build project and you are good to go.

## TODO

* Add refresh functionality.
* Integrate pagination.
* Create news details page.
* Add tests.

## Screenshots

<img src="https://raw.githubusercontent.com/crazyhitty/News/master/screenshots/1.png" alt="alt text" width="400">