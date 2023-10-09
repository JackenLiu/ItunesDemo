# Welcome to use ItunesDemo [(Download Apk)](https://github.com/JackenLiu/ItunesDemo/blob/main/file/app-debug.apk)

An Android app that allows users to search for songs, albums, or artists using the iTunes API. The app provide the ability to filter and bookmark songs. It supports English, Traditional Chinese (Hong Kong), and Simplified Chinese (China) languages.

---

## How to use

**Just Watch a gif**：

![avatar](https://github.com/JackenLiu/ItunesDemo/blob/main/file/preview.gif)

ㅤ
ㅤ
ㅤ
ㅤ
ㅤ
ㅤ


**Enter keywords to perform a search**：

After obtaining search results, the search box will display media and country categories below it. Clicking on these categories performs intersection filtering. For example, if you click "USA" and then "song," it filters out all songs from the USA. However, if you click "movie" and then "song", no results will be displayed. Clicking "song" alone filters out all songs, as shown in the image below.

![avatar](https://github.com/JackenLiu/ItunesDemo/blob/main/file/Screenshot_1.jpg)
ㅤ
ㅤ
ㅤ
ㅤ
ㅤ
ㅤ
ㅤ


**In the music player, you can click on a song to listen to a preview of the song.**：

For search results, if the media type is a song, clicking on the item will trigger a song playback popup for previewing the song. The same applies to the Favorites section. However, if the media type is not a song, as shown in the image below

![avatar](https://github.com/JackenLiu/ItunesDemo/blob/main/file/Screenshot_2.jpg)

ㅤ
ㅤ
ㅤ
ㅤ
ㅤ
ㅤ


**Switching between multiple pages of search results.**：

In search results, you can navigate through multiple pages. Scrolling up displays the previous 20 results, while scrolling down shows the next 20 results. Each viewable set of results consists of 20 items, as illustrated in the following image.

![avatar](https://github.com/JackenLiu/ItunesDemo/blob/main/file/Screenshot_3.jpg)
ㅤ
ㅤ
ㅤ
ㅤ
ㅤ
ㅤ

## Project Architecture

· Use MVVM app architecture，retrofit2 + ViewModel + livedata<br>
· Use OkHttp interceptor checkout http log<br>
· Use glide library  to show the image<br>
· Use RecyclerView to show the data list<br>
· Use SmartRefreshLayout library to load last page / next page data<br>
· Use room library to achieve the favorites function<br>
· Use junit、espresso and mockwebserver to achieve function、ui and network unit tests
ㅤ
ㅤ
ㅤ
ㅤ
ㅤ
ㅤ
ㅤ
ㅤ
ㅤ

## Project challenges

**search filtering**：<br>
· When filtering by country and media type, because it's an intersection filter, the selected types also need to exclude the unselected types. For example, if I select songs from the United States, I need to exclude movies and albums from China.<br>
· When I select the "United States" label, it may include songs, movies, and albums under the United States category. So when I click on "songs," I need to remove movies and albums from the United States category.<br>
· Therefore, each time a filtering label is clicked by the user, a targeted removal is performed on all the original results. That means, when "United States" is selected and then "songs" is clicked, it directly applies the filtering conditions to the original data, rather than extracting songs from the United States separately. <br>
· This approach allows for a quick filtering function, especially when dealing with limited search results.

ㅤ
ㅤ
ㅤ
ㅤ

**multi-page loading**：<br>
· In the iTunes API, there are two parameters, "offset" and "limit." Among them, "limit" specifies the number of search results, while "offset" represents the offset.<br>
· To implement pagination functionality, I chose the method of loading content by scrolling up and down. Whenever a new page is loaded, the offset is incremented by 20. Similarly, when loading the previous page, the offset is decreased by 20. When the offset is 0, no new API requests are made.<br>

ㅤ
ㅤ
ㅤ

**tap animation**：<br>
· When clicking on media of a song type, you can notice that its image has a transition animation. It utilizes the functionality of shared elements, allowing the shared display of controls between different activities, along with a transition animation.<br>

ㅤ
ㅤ
ㅤ
ㅤ
ㅤ
ㅤ

