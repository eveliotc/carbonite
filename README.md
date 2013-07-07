carbonite
=========

A simple in memory and persistent `Object` cache for Android.

![Carbonite Android Logo](extra/logo/carbonite-android.png?raw=true)

<hr/>

Carbonite aims to deal with your data POJOs (JavaBeans folks anyone?) without boilerplate code, so you can forgforgetuet about ORMs, SQLite, `Cursor`s, `ContentProvider`s, etc. for data that you already hold in `Object`s anyways.

Although it can be used as the only persistence solution on Android, *it is not* one of carbonite goals to do so, you should evaluate when traditional persistence solutions make more sense based on your problem.

**Note**: Carbonite is currently under heavy first version development so API and stuff might change among versions, please bear with us.

### How does it work?

Carbonite keeps your POJOs in memory while transparently persisting them in background to storage, you can retrieve them later either from memory or loading them asynchronously/synchronously from storage.

### Usage
1. Include it in your project. 

`TODO info regarding jar, maven, gradle, etc.`.

2. Build your carbonite instance:
```java
  Carbonite.using(context) /* `getApplicationContext()` is used and not retained */
        .retaining(YourPojo.class)
        .in(MEMORY) /* optional */
        .and(STORAGE) /* optional */
        /* This can be replaced by just `build()` */
        .iLoveYou() /* Does nothing */
        .iKnow(); // calls `build()`
```

3. Use it:
##### set
```java
  YourPojo data = …
  ...
  carbonite.set("data", data); // will keep it in memory and storage
```
You can also use `memory` and `storage`.
##### get
From memory:
```java
  YourPojo stored = carbonite.memory("data", YourPojo.class);
```
From storage:
```java
  YourPojo stored = carbonite.storage("data", YourPojo.class);
```
From memory or storage:
```java
  Future<YourPojo> future = carbonite.get("data", YourPojo.class);
  …
  YourPojo stored = future.get();
```

### Goals
- Simplicity (DRY, YAGNI, etc.)
- Zero boilerplate code
- Fast
- Reliable
- Optimized for Android
- Easy and fun to use


### Roadmap
This is a raw short term roadmap of features that I'd like to see in Carbonite:

- A sample app (in progress)
- Optional unsafe `get`, `retaining`, etc. methods without `Class` param.
- Future listeners
- Auto keys (in POJO -interface, annotations, code generation, `hashCode()` maybe baby- or using a third object to provide it)
- Bulk operations
- Eviction (allow using weigher, etc. for auto eviction)
- CRUD notifications (e.g. using listeners or events)
- Auto update references
- Object pooling
- Stats
- Help with Carbonite instances hold by life cycle objects (`Fragment`, `Activity`, etc.).
- Allow more cache (in memory/storage TTL, references, etc.) and serialization (JSON, Java Serialization, etc.) implementations.


### About
Brought to you by the Carbonite contributors and [this guy](http://gplus.to/eveliotc).

Carbonite relies (yet totally optional) on the following awesome open source software:

- [kryo](https://code.google.com/p/kryo)
- [DiskLruCache](https://github.com/JakeWharton/DiskLruCache)

### License
```
Copyright 2013 Evelio Tarazona Cáceres <evelio@evelio.info>
Copyright 2013 Carbonite contributors <contributors@evelio.info>

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```
