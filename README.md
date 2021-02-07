# presentation-layer-sample

Это проект - пример реализации presentation слоя. 

## Открытые вопросы

* Что такое единый стейт? Если экран сложный, то как описывать сложные стейты. Если через seald class, то по какому принципу описывать seald class со стейтом?
* Как работать с single liveData (map/distinct?)
* Как работать с command liveData? Нужно разделить общие команды и приватные для каждого экрана.
* Как подружить liveData и state-delegate? Какую сделать структуры обертки для загружаемых данных(Loading, Content, Error).
* Как обрабатывать пересоздание диалогов (как подсунуть новую лямбду)
Нужно посмотреть [сюда](https://developer.android.com/training/basics/fragments/pass-data-between).

## Пожелания

* Использовать: mapDistinct
* Использовать: viewBinding
* Делать маппинг данных к Item для группи во ViewModel, чтобы ViewModel ничего не знала об item'ах
* Название публичных методов ViewModel в информирубщем стиле ( `onLoginClicked` )

## Решение

### Описание проблемы

Если использовать VM + livedata то возникает проблема с синхронизацией нескольких LiveData. Поэтому приходится использовать единый стейт и единственный метод его отрисовки `render(state)` . Этот паттерн назвается **Model-View-Update (MVU)** и основан он на **Unidirectional data flow (UDF)**. Есть несколько реализаций UDF. Самые популярные - это **TEA(The Elm Architectire)**, **MVI**, **Redux** и **Flux**. И многие ребята которые о них рассказывают сами путаются в различиях.

Во всех из них используется единый стейт и сайд эффект/команда/евент (везде называется по разному, но суть SingleEventLiveData).

### Работа с единым стейтом

Единый стейт удобно тестировать, но при его использовании вздникают другие проблемы:

1) Как обновлять единый стейт? Если View передает все дейтвия во ViewModel/Presenter, то обновление стейта может проихожить во многих местах. Из-за этого, на сложных экранах, появляются состояния гонки и новый стейт может перезатереть другой новый стейт.  
Решений этой проблемы 2:  

    1) Конвертировать все действия в единую шину (Action) и на основе этой шины делать reducer, который применяет действие с Action в стейт.  

        * **Плюсы:** все изменения в одном месте из проще читать и понимать.
        * **Минусы:** очень много кода получается, в котором особой логики нет. Можно использовать генераторы кода, например [actions-dispatcher](https://github.com/rougsig/actions-dispatcher), но тогда время компиляции увеличивается и это грустно.

    2) Написать потокобезопасную утилиту обновления стейта, в которой сначала всегда будет обновление стейта, а потом получение. [Пример из MvRx](https://github.com/airbnb/MvRx/blob/master/mvrx/src/main/kotlin/com/airbnb/mvrx/RealMvRxStateStore.kt).  

        * **Плюсы:** Руки развязаны, можно обновлять где угодно. Кода меньше.
        * **Минусы:** Стейты можно обновлять где угодно, нет четкого контроля. Можно написать плохо.

### Работа с сайд эффектами

Сайд еффект - это одноразовое событие: навигация, отображение snackbar, диалога и обработка интентов (шаринг, intent chooser и т.п.).

Основная проблема при использовании сайд эффектов это дублирование кода. Какие-то сайд эффекты являются общими для всех экранов, а какие-то нет. Решение - вынесение работы с сайд эффектами в базовый класс и переопределение обработчика локально, там где это требуется.

### Состояния экрана

Для изменения состояний экрана у нас используется [state-delegator](https://github.com/RedMadRobot/state-delegator/tree/master/state-delegator). Он позволяет объединить несколько view в группу и менять их состояния 1 строчкой. Ну и самое важное - это возможность добавления анимации перехода из одного состояния в другое.

Мне на моем проекте недостаточно было набора состояний `Loading Content Error` , поэтому я расширил этот набор и получил следующие состояния (ViewState):

* `Content, Progress` - используется для отправки запроса на сервер (авторизация).
* `Loading, Content, Error` - получение данных от сервера, где отсутвует состояние отсутвия данных.
* `Loading, Content, Stub, Error` - Получение данных от сервера, где нужна обработка состояния отсутвия данных.
* `Content, Error` - получение данных из кэша, где состояние загрузки не требуется, но требуется обработка ошибок.
* `Content, Stub, Error` - получение данных из кэша, где состояние загрузки не требуется, но требуется обработка ошибок и отсутвия данных.
* `Shimmer, Content, Progress, Stub, Error` - Самый сложный набор состояний, в которм идет получение данных с сервера и отправка изменненых данных на вервер. По сути это объединение 1 и 3 набора состояний.

В этом подходе есть 2 проблемы:

1) В каждом варианте может присутствовать pull to refresh, который усложнаяет логику работы с ViewState.
2) Появляется 2 стейте ViewState (delegator) и ViewState (Данные во VM) xD. Их можно попробовать объединить, но тогда VM начинает зависить от состояния view, что не очень хорошо.
3) Некоторые экран тербуют использования recycler-base подхода. То есть весь экран реализован в виде списка. Из-за этого происходит смещение подходов обычного использования view со state-delegator и частичного его использования в связке с RecyclerView.

## Список литературы и полезных материалов

### Сторонние решения

#### Model-View-Update (MVU)

* [Model view update на примере F#](https://thomasbandt.com/model-view-update)

#### Unidirectional data flow (UDF)

* [Android Unidirectional Data Flow with LiveData](https://medium.com/hackernoon/android-unidirectional-flow-with-livedata-bf24119e747)

#### Model-View-Intent (MVI)

* [Сергей Рябов — Как приготовить хорошо прожаренный MVI под Android](https://youtu.be/hBkQkjWnAjg)

* MVI от Hannes Dorfmann

  * Оригинальный цикл из 8-ми статей

    * [Hannes Dorfmann: Основы MVI](http://hannesdorfmann.com/android/model-view-intent)
    * [Hannes Dorfmann: Reactive Apps with Model-View-Intent - Part1 - Model](http://hannesdorfmann.com/android/mosby3-mvi-1)
    * [Hannes Dorfmann: Reactive Apps with Model-View-Intent - Part2 - View and Intent](http://hannesdorfmann.com/android/mosby3-mvi-2)
    * [Hannes Dorfmann: Reactive Apps with Model-View-Intent - Part3 - State Reducer](http://hannesdorfmann.com/android/mosby3-mvi-3)
    * [Hannes Dorfmann: Reactive Apps with Model-View-Intent - Part4 - Independent UI Components](http://hannesdorfmann.com/android/mosby3-mvi-4)
    * [Hannes Dorfmann: Reactive Apps with Model-View-Intent - Part5 - Debugging with ease](http://hannesdorfmann.com/android/mosby3-mvi-5)
    * [Hannes Dorfmann: Reactive Apps with Model-View-Intent - Part6 - Restoring State](http://hannesdorfmann.com/android/mosby3-mvi-6)
    * [Hannes Dorfmann: Reactive Apps with Model-View-Intent - Part7 - Timing (SingleLiveEvent problem)](http://hannesdorfmann.com/android/mosby3-mvi-7)
    * [Hannes Dorfmann: Reactive Apps with Model-View-Intent - Part8 - Navigation](http://hannesdorfmann.com/android/mosby3-mvi-8)

  * Перевод от Тинькофф (пока только 3 статьи)

    * [Реактивные приложения с Model-View-Intent. Часть 1: Модель](https://habr.com/ru/company/tinkoff/blog/325376/)
    * [Реактивные приложения с Model-View-Intent. Часть 2: View и Intent](https://habr.com/ru/company/tinkoff/blog/338558/)
    * [Реактивные приложения с Model-View-Intent. Часть 3: State Reducer](https://habr.com/ru/company/tinkoff/blog/348908/)

* [Raywenderlich: MVI Architecture for Android Tutorial: Getting Started](https://www.raywenderlich.com/817602-mvi-architecture-for-android-tutorial-getting-started)
* [Android MVI Pattern. MVI Basics](https://caster.io/lessons/android-mvi-mvi-basics)
* [Part 1: Reactive architectures. MVI + RxJava](https://android.jlelse.eu/reactive-architectures-part-1-mvi-rxjava-af7791245d48)
* [MVI Pattern For Android In 4 Steps](https://www.linkedin.com/pulse/mvi-pattern-android-4-steps-ahmed-adel-ismail/)
* [MVI — another member of the MV* band](https://proandroiddev.com/mvi-a-new-member-of-the-mv-band-6f7f0d23bc8a)
* [MVI - a Reactive Architecture Pattern](https://medium.com/quality-content/mvi-a-reactive-architecture-pattern-45c6f5096ab7)

#### MVI от Badoo

* [Все тайны MVI (Интервью от одного из авторов MVICore)](https://youtu.be/9NYgRODhkdw)
* [Аналог MVICore от того же автора. Проще чем MVICore](https://arkivanov.github.io/MVIKotlin/)
* [Современная MVI-архитектура на базе Kotlin](https://habr.com/ru/company/badoo/blog/429728/)
* [Архитектурный шаблон MVI в Kotlin Multiplatform, часть 1](https://habr.com/ru/company/badoo/blog/501968/)
* [Видео пример того как отлаживать приложения с IntelliJ IDEA "MVIKotlin time travel" plugin](https://youtu.be/Tr2ayOcVU34)

#### TEA (The Elm Architecture)

* [Taming state in Android with Elm Architecture and Kotlin, Part 1](https://proandroiddev.com/taming-state-in-android-with-elm-architecture-and-kotlin-part-1-566caae0f706)

#### MVPM

* [Реактивные приложения с паттерном RxPM. Прощайте​ MVP и MVVM](https://habr.com/ru/company/mobileup/blog/326962/)
* [Martin Fowler: Presentation Model](https://www.martinfowler.com/eaaDev/PresentationModel.html)

#### Прочие решения

* [Simplifying Jetpack Navigation between top-level destinations using Dagger-Hilt](https://itnext.io/simplifying-jetpack-navigation-between-top-level-destinations-using-dagger-hilt-3d918721d91e)
* [Android LiveData на Kotlin с использованием Retrofit и coroutines](https://habr.com/ru/post/427475/)
* [Effective LiveData and ViewModel Testing](https://android.jlelse.eu/effective-livedata-and-viewmodel-testing-17f25069fcd4)
* [LiveData & ViewModel — Making your own magic](https://medium.com/mindorks/livedata-viewmodel-making-your-own-magic-73facb06fbb)
* [LiveData with Coroutines and Flow (Android Dev Summit '19)](https://www.youtube.com/watch?v=B8ppnjGPAGE)
* [When to load data in ViewModels](https://proandroiddev.com/when-to-load-data-in-viewmodels-ad9616940da7)
* [Architecture Components: Easy Mapping of Actions and UI State](https://android.jlelse.eu/architecture-components-easy-mapping-of-actions-and-ui-state-207663e3fd)
* [LiveData with single events](https://proandroiddev.com/livedata-with-single-events-2395dea972a8)
* [Android: Understand LiveData](https://medium.com/better-programming/everything-to-should-understand-about-livedata-507dd83adea7)
* [ViewModel и LiveData: паттерны и антипаттерны](https://habr.com/ru/post/338590/)
* [События на базе LiveData Android](https://habr.com/ru/post/468749/)
* [ViewModels and LiveData: Patterns + AntiPatterns](https://medium.com/androiddevelopers/viewmodels-and-livedata-patterns-antipatterns-21efaef74a54)
* [Designing and Working with Single View States on Android](https://zsmb.co/designing-and-working-with-single-view-states-on-android/)
* [PresentationModel](https://martinfowler.com/eaaDev/PresentationModel.html)
* [Redux против MobX без путаницы](https://habr.com/ru/post/489854/#whats-the-difference-between-redux-and-mobx)

### Примеры

#### Примеры с других проектов

* [пример от Ромы](https://github.com/Zestxx/alter_mvi_sample)
* [пример работы с mapDistinct](https://gist.github.com/osipxd/bb5aee5fb2aec77f4a0e9af33fa089fa)
* [ViewModel and LiveData extensions (немного скорректированные гисты из базового проекта)](https://gist.github.com/osipxd/26fa3ce564c47cf0113ce492209c2d7b)
* [экстеншены для ViewBinding + делегат](https://gist.github.com/osipxd/1d5d7096f0e62793c7093297e0b98b09)
* [Примедение string и res id строки в ресурсах к одному типу, что позволяет работать с ними одинаково.](https://gist.github.com/Shipaaaa/7c62cf048cb46fbf4990d5bbcd70edc9)
* [Вариант обертки для обработки ошибок](https://gist.github.com/kirich1409/9cf26f778c3357f61525e67723df984b)

#### Сторонние примеры

* [Example MVI implementation, based off of Google's architectural samples.](https://github.com/kanawish/android-mvi-sample)
* [android-showcase](https://github.com/igorwojda/android-showcase)
* [workflow-kotlin](https://github.com/square/workflow-kotlin)
* [Pokedex](https://github.com/skydoves/Pokedex)
* [ModelWatcher](https://github.com/badoo/MVICore/blob/master/mvicore-diff/src/main/java/com/badoo/mvicore/ModelWatcher.kt)
* [oolong](https://github.com/oolong-kt/oolong/) - MVU for Kotlin Multiplatform

#### Полезные утилиты

* [actions-dispatcher](https://github.com/rougsig/actions-dispatcher) - кодогенерация для MVI.
* [file-template-loader](https://github.com/rougsig/file-template-loader/wiki) - Плагин позволяет упростить создание шаблонов для проектов, которые разрабатываются с помощью IDE от JetBrains: Android Studio, IntelliJ IDEA и т.д.
* [Tinder StateMachine](https://github.com/Tinder/StateMachine) - A Kotlin DSL for finite state machine.
* [RxRedux by Freeletics](https://github.com/freeletics/RxRedux)
* [MvRx by Airbnb](https://github.com/airbnb/MvRx)
* [Mobius by Spotify](https://github.com/spotify/mobius)
