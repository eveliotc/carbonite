carbonite
=========

A simple in memory and persistent `Object` cache for Android.

![Carbonite Android Logo](extra/logo/carbonite-android.png?raw=true)

<hr/>

Carbonite aims to deal with your data POJOs (JavaBeans folks anyone?) without boilerplate code, so you can forget about
ORMs, SQLite, `Cursor`s, `ContentProvider`s, etc. for data that you already hold in `Object`s anyways, plus you want them evicted at some point therefore losing them is not really a big deal, yet you can control how that will happen.

Although it can be used as the only persistence solution on Android, *it is not* one of carbonite goals to do so,
you should evaluate when traditional persistence solutions make more sense based on your problem.

**Note**: Carbonite is currently under heavy first version development so API and stuff might change among versions,
please bear with us.

### How does it work?

Carbonite keeps your POJOs in memory while transparently persisting them in background to storage, you can retrieve them
 later either from memory or loading them asynchronously/synchronously from storage. You can specify how and how long
 are them kept, for finer control you can provide your own implementations.
 
Current implementations vary, all of them are stale values prone if you do not properly use them.

`TODO provide docs regarding current implementations and how to use them, etc.`

### Usage
1. Include it in your project. 
`TODO info regarding jar, maven, gradle, etc.`


2. Build your carbonite instance:
```java
  Carbonite.using(context) /* getApplicationContext() is used and not retained */
        .retaining(YourPojo.class)
        .in(MEMORY) /* optional, default */
        .and(STORAGE) /* optional if you don't want to keep in memory */
        /* This can be replaced by just build() */
        .iLoveYou() /* Does nothing */
        .iKnow(); // calls build()
```

3. Use it:
##### set
```java
  YourPojo data = …
  ...
  carbonite.set("data", data); // will keep it in memory and async persist it to storage
```
You can also use `memory` and `storage` for a blocking way.
##### get
From memory:
```java
  YourPojo stored = carbonite.memory("data", YourPojo.class);
```
From storage (blocking):
```java
  YourPojo stored = carbonite.storage("data", YourPojo.class);
```
From memory (blocking) or storage (async):
```java
  Future<YourPojo> future = carbonite.get("data", YourPojo.class);
  …
  YourPojo stored = future.get();
```

**Notes**:

- `MEMORY` operations will always happen in calling thread as often will be as simple as `put`/`get` an item in/from a `Map`, while `STORAGE` operations happen asynchronously for `set` and `get` and are blocking for `storage` calls.


### Goals
- Simplicity (DRY, YAGNI, etc.)
- Zero boilerplate code
- Fast
- Dependencies free
- Optimized for Android
- Easy and fun to use


### Roadmap
This is a raw short term roadmap of features that I'd like to see in Carbonite:

- Document all the `public` APIs
- A sample app (in progress)
- Help with Carbonite instances hold by life cycle objects (`Fragment`, `Activity`, etc.).
- Future listeners so we can remove `Future` boilerplate, allow callback cleanup for objects with life cycles like the ones mentioned above.
- Auto keys (in POJO -interface, annotations, code generation, `hashCode()` maybe baby- or using a third object to provide it)
- Bulk operations
- Eviction (allow using weigher, etc. for auto eviction)
  - Some cache implemenations (Like LRU or weak referenced ones) already do this based it is own rules.
- CRUD notifications (e.g. using listeners or events)
- Auto update references (to deal with stale objects)
- Object pooling
- Stats
- Add more cache (in memory/storage TTL, references, etc.) and serialization (JSON, Java Serialization, etc.) implementations.
  - You can provide your own using a `CacheFactory` or providing `CacheOptions` on build time.
  - For `StorageLruCache` you can provide your own `Serializer` using `StorageLruCache.Options`.
- Optional unsafe `get`, `retaining`, etc. methods without `Class` param.

### About
Brought to you by the Carbonite contributors specially [this guy](http://gplus.to/eveliotc).

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
