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

## License

```
MIT License

Copyright (c) 2018 Kartik Sharma

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

```
