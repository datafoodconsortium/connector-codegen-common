# Data Food Consortium connector code generation common utilities

This repository contains resource files, useful for generating the source code of the Data Food Consortium (DFC) connector into different programming languages.

Indeed, the DFC connector source code is intented to be generated from an UML model. This step is called "model to text" transformation or "M2T". To make such a transformation we need a "source code generator": a program able to write source code (called "text") from a schema or plan (called "model"). To ease this process, we choose to use Eclipse with the Acceleo framework. Here, we provide some Acceleo functions that will help with building this "source code generator".

## What is the DFC connector?

The DFC connector is a tool designed to help developers implement the [DFC standard](https://datafoodconsortium.org) in their applications. It provides a data model (abstraction layer) to easily express semantic data conforming to the DFC ontology. The connector thus hides the complexity of creating a semantic application because developers do not need to acquire any particular semantic web skills.

## What's included

This repository contains an Eclipse project that can be used within your code generator project.

It provides several helper methods:
- createGeneralization(aClass: Class): Boolean
- getTypeName(aType: Type) : String
- isConstructor(o: Operation): Boolean 
- isInitializer(p: Parameter): Boolean 
- isInitializerParent(p: Parameter): Boolean 
- isGetter(o: Operation): Boolean 
- isSetter(o: Operation): Boolean 
- isAdder(o: Operation): Boolean 
- isRemover(o: Operation): Boolean 
- isSemantic(e: Element): Boolean 
- isBlankNode(e: Element): Boolean 
- isExternal(e: Element): Boolean 
- isPrimitive(t: Type): Boolean 
- hasStereotype(e: Element, s: String): Boolean 
- getAttributeForStereotypedOperation(c: Class, s: String, o: String): String 
- getConstructorUninitializedAttributes(o: Operation): Sequence(Property)
- getConstructor(c: Class, o: Operation): String 
- getGetter(c: Class, o: Operation): String 
- getSetter(c: Class, o: Operation): String 
- getAdder(c: Class, o: Operation): String 
- getRemover(c: Class, o: Operation): String 
- getGetter(p: Property): String

You have to include the `common.mtl` file in your generator.

A method can be called by itself or on an object like:
- `isGetter(operation)` or
- `operation.isGetter()`
