# Common resources to build the DFC connector

This repository contains resource files, useful for generating the source code of the Data Food Consortium (DFC) connector into different programming languages.

Indeed, the DFC connector source code is intented to be generated from an UML model. This step is called "model to text" transformation or "M2T". To make such a transformation we need a "source code generator": a program able to write source code (called "text") from a schema or plan (called "model"). To ease this process, we choose to use Eclipse with the Acceleo framework. Here, we provide some Acceleo functions that will help with building this "source code generator".

## What is the DFC connector?

The DFC connector is a tool designed to help developers implement the [DFC standard](https://datafoodconsortium.org) in their applications. It provides a data model (abstraction layer) to easily express semantic data conforming to the DFC ontology. The connector thus hides the complexity of creating a semantic application because developers do not need to acquire any particular semantic web skills.
