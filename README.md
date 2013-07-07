carbonite
=========

A simple in memory and persistent `Object` cache for Android.

![Carbonite Android Logo](extra/logo/carbonite-android.png?raw=true)

<hr/>

Carbonite aims to deal with your data POJOs (JavaBeans folks anyone?) without boilerplate code, so you can forguet about ORMs, SQLite, `Cursor`s, `ContentProvider`s, etc. for data that you arleady hold in `Object`s anyways.

Although it can be used as the only persistence solution on Android, is not one of carbonite goals to do so, you should evaluate when traditional persistence solutions make more sense based in your problem.

**Note**: Carbonite is currently under heavy first version development so API and such might change among versions.

### How does it work?

Carbonite keeps your POJOs in memory while transparently persisting them in background to storage, you can retrieve them later either from memory or loading them asynchronously/synchronously from storage.

### Usage
1. Include it in your project. 
`TODO info regarding jar, maven, gradle, etc.`.

2. Build your carbonite instance:
```java
  Carbonite.using(context)
    .retaining(YourPojo.class)
    .in(MEMORY)
    .and(STORAGE)
    .iLoveYou()
    .iKnow();
```

3. Use it:
##### set
```java
  YourPojo data = …
  ...
  carbonite.set("data", data); // will keep it in memory and storage
```
##### get
From memory:
```java
  YourPojo stored = carbonite.memory("data");
```
From memory or storage:
```java
  Future<YourPojo> future = carbonite.get("data");
  …
  YourPojo stored = future.get();
```

### Goals
Carbonite aims to be/have:

- Simple
- Zero boilerplate on your end
- Fast
- Reliable
- Optimized for Android
- Easy and fun to use


### Roadmap
This is a raw short term roadmap of features that I'd like to see in Carbonite:

- A sample app (in progress)
- Future listeners
- Auto keys (in POJO -interface, annotations, code generation, hashcode maybe baby- or using a third object to provide it)
- Bulk operations
- Eviction (allow using weighters, etc. for auto eviction)
- CRUD notifications (e.g. using listeners or events)
- Auto update references
- Object pooling
- Stats
- Help with Lifecyle instances (fragments, activities, etc.) to evict no longer needed objects.
- Allow more cache (TTL, references, etc.) and serialization (JSON, Java Serialization, etc.) implementations



### About
Brought to you by [this guy](http://gplus.to/eveliotc) and the Carbonite contributors.

Carbonite relies (yet totally optional) in the following awesome open source software:

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
