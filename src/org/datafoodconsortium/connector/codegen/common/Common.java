package org.datafoodconsortium.connector.codegen.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.common.util.WrappedException;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.resource.UMLResource;
import org.eclipse.uml2.uml.resources.util.UMLResourcesUtil;

public class Common {
	
	public interface Profiles {
		public static final String CONNECTOR = "datafoodconsortium_connector";
	}
	
	public interface Stereotypes {
		public static final String PROPERTY = "property";
		public static final String PROPERTY_MULTIPLE = "propertyMultiple";
		public static final String CONSTRUCTOR = "constructor";
		public static final String INITIALIZER = "initializer";
		public static final String INITIALIZER_PARENT = "initializerParent";
		public static final String GETTER = "getter";
		public static final String SETTER = "setter";
		public static final String ADDER = "adder";
		public static final String REMOVER = "remover";
		public static final String SEMANTIC = "semantic";
		public static final String BLANK_NODE = "blankNode";
		public static final String EXTERNAL = "external";
	}
	
	static final ResourceSet RESOURCE_SET;
	static final Package PRIMITIVE_TYPES;
	
	static {
		// Create a resource-set to contain the resource(s) that we load and
        // save
		RESOURCE_SET = new ResourceSetImpl();

        // Initialize registrations of resource factories, library models,
        // profiles, Ecore metadata, and other dependencies required for
        // serializing and working with UML resources. This is only necessary in
        // applications that are not hosted in the Eclipse platform run-time, in
        // which case these registrations are discovered automatically from
        // Eclipse extension points.
        UMLResourcesUtil.init(RESOURCE_SET);
        
        PRIMITIVE_TYPES = load(URI.createURI(UMLResource.UML_PRIMITIVE_TYPES_LIBRARY_URI));
	}
	
	public static Package load(URI uri) {
        Package loadedPackage = null;

        try {
            // Load the requested resource
            Resource resource = RESOURCE_SET.getResource(uri, true);

            // Get the first (should be only) package from it
            loadedPackage = (Package) EcoreUtil.getObjectByType(resource.getContents(), UMLPackage.Literals.PACKAGE);
        } catch (WrappedException we) {
            System.err.println(we.getMessage());
            // System.exit(1);
        }

        return loadedPackage;
    }
	
	public static String getStereotypeName(String profile, String stereotype) {
		return profile + "::" + stereotype;
	}
	
	public Operation getConstructorOperation(Class constructible, Stereotype stereotype) throws Exception {
		for (Operation operation : constructible.getOwnedOperations()) {
			if (operation.isStereotypeApplied(stereotype))
				return operation;
		}
		throw new Exception("The class " + constructible.getName() + " has no constructor operation.");
	}
	
	public boolean isPrimitive(Type literal) {
		String[] literals = { "Boolean", "String", "Real", "Integer" };
		return Arrays.asList(literals).contains(literal.getName());
	}
	
	public String getTypeName(Type aType) {
		String typeName = aType.getName();
		if (typeName.equals("Boolean"))
			return "bool";
		else if (typeName.equals("String"))
			return "string";
		else if (typeName.equals("Real"))
			return "number";
		else if (typeName.equals("Integer"))
			return "number";
		return typeName;
	}
	
	public boolean isProperty(Property property) {
		return hasStereotype(property, Stereotypes.PROPERTY);
	}
	
	public boolean isPropertyMultiple(Property property) {
		return hasStereotype(property, Stereotypes.PROPERTY_MULTIPLE);
	}
	
	public boolean isConstructor(Operation operation) {
		return hasStereotype(operation, Stereotypes.CONSTRUCTOR);
	}
	
	public boolean isInitializer(Parameter parameter) {
		return hasStereotype(parameter, Stereotypes.INITIALIZER);
	}
	
	public boolean isInitializerParent(Parameter parameter) {
		return hasStereotype(parameter, Stereotypes.INITIALIZER_PARENT);
	}
	
	public boolean isGetter(Operation operation) {
		return hasStereotype(operation, Stereotypes.GETTER);
	}
	
	public boolean isSetter(Operation operation) {
		return hasStereotype(operation, Stereotypes.SETTER);
	}
	
	public boolean isAdder(Operation operation) {
		return hasStereotype(operation, Stereotypes.ADDER);
	}
	
	public boolean isRemover(Operation operation) {
		return hasStereotype(operation, Stereotypes.REMOVER);
	}
	
	public boolean isSemantic(Element element) {
		return hasStereotype(element, Stereotypes.SEMANTIC);
	}
	
	public boolean isBlankNode(Element element) {
		return hasStereotype(element, Stereotypes.BLANK_NODE);
	}
	
	public boolean isExternal(Element element) {
		return hasStereotype(element, Stereotypes.EXTERNAL);
	}
	
	public boolean hasStereotype(Element element, String stereotypeName) {
		List<Stereotype> stereotypes = element.getAppliedStereotypes();
		for (Stereotype stereotype : stereotypes) {
			if (stereotype.getName().equals(stereotypeName)) {
				return true;
			}
		}
		return false;
	}
	
	public Object getStereotypeValue(NamedElement element, String stereotypeName, String attributeName) {
		Object result = null;
		List<Stereotype> stereotypes = element.getAppliedStereotypes();
		for (Stereotype stereotype : stereotypes) {
			if (stereotype.getName().equals(stereotypeName)) {
				try {
					result = element.getValue(stereotype, attributeName);
				} catch(Exception e) {
					System.err.println("Failed to getStereotypeValue of stereotype " + stereotypeName + " for " + element.getName());
				}
			}
		}
		return result;
	}
	
	public Operation getSetter(Parameter parameter) {
		Property property = (Property) this.getStereotypeValue(parameter, Stereotypes.INITIALIZER, "property");
		
		if (property != null) {
			Operation setter = (Operation) this.getStereotypeValue(property, Stereotypes.PROPERTY, "setter");
			return setter;
		}
		
		return null;
	}
	
	public Operation getAdder(Parameter parameter) {
		Property property = (Property) this.getStereotypeValue(parameter, Stereotypes.INITIALIZER, "property");
		
		if (property != null) {
			Operation adder = (Operation) this.getStereotypeValue(property, Stereotypes.PROPERTY_MULTIPLE, "adder");
			return adder;
		}
		
		return null;
	}
	
	public String getGetter(Property property) {
		String propertyType = isProperty(property)? Stereotypes.PROPERTY : Stereotypes.PROPERTY_MULTIPLE;
		Object result = getStereotypeValue(property, propertyType, Stereotypes.GETTER);
		if (result == null) {
			NamedElement owner = (NamedElement) property.getOwner();
			System.err.println("Failed to get getter of property " + owner.getName() + "::" + property.getName());
		}
		return result != null ? ((Operation)result).getName() : "";
	}
	
	public String getConstructor(Class aClass, Operation operation) {
		return getAttributeForStereotypedOperation(aClass, Stereotypes.CONSTRUCTOR, operation.getName());
	}
	
	public String getGetter(Class aClass, Operation operation) {
		return getAttributeForStereotypedOperation(aClass, Stereotypes.GETTER, operation.getName());
	}
	
	public String getSetter(Class aClass, Operation operation) {
		return getAttributeForStereotypedOperation(aClass, Stereotypes.SETTER, operation.getName());
	}
	
	public Operation getAdder(Property property) {
		String propertyType = isProperty(property)? Stereotypes.PROPERTY : Stereotypes.PROPERTY_MULTIPLE;
		Object result = getStereotypeValue(property, propertyType, Stereotypes.ADDER);
		if (result == null) {
			NamedElement owner = (NamedElement) property.getOwner();
			System.err.println("Failed to get adder of property " + owner.getName() + "::" + property.getName());
		}
		return result != null ? (Operation) result: null;
	}
	
	public Operation getSetter(Property property) {
		String propertyType = isProperty(property)? Stereotypes.PROPERTY : Stereotypes.PROPERTY_MULTIPLE;
		Object result = getStereotypeValue(property, propertyType, Stereotypes.SETTER);
		if (result == null) {
			NamedElement owner = (NamedElement) property.getOwner();
			System.err.println("Failed to get setter of property " + owner.getName() + "::" + property.getName());
		}
		return result != null ? (Operation) result: null;
	}
	
	public String getAdder(Class aClass, Operation operation) {
		return getAttributeForStereotypedOperation(aClass, Stereotypes.ADDER, operation.getName());
	}
	
	public Property getPropertyOfAdder(Class aClass, Operation o) {
		for (Property property : aClass.getOwnedAttributes()) {
			Operation setter = null;
			if (this.isProperty(property))
				setter = (Operation) this.getStereotypeValue(property, Stereotypes.PROPERTY, "adder");
			else if (this.isPropertyMultiple(property))
				setter = (Operation) this.getStereotypeValue(property, Stereotypes.PROPERTY_MULTIPLE, "adder");
			if (o.equals(setter))
				return property;
		}
		return null;
	}
	
	public Property getPropertyOfGetter(Class aClass, Operation o) {
		for (Property property : aClass.getOwnedAttributes()) {
			Operation setter = null;
			if (this.isProperty(property))
				setter = (Operation) this.getStereotypeValue(property, Stereotypes.PROPERTY, "getter");
			else if (this.isPropertyMultiple(property))
				setter = (Operation) this.getStereotypeValue(property, Stereotypes.PROPERTY_MULTIPLE, "getter");
			if (o.equals(setter))
				return property;
		}
		return null;
	}
	
	public Property getPropertyOfSetter(Class aClass, Operation o) {
		for (Property property : aClass.getOwnedAttributes()) {
			Operation setter = null;
			if (this.isProperty(property))
				setter = (Operation) this.getStereotypeValue(property, Stereotypes.PROPERTY, "setter");
			else if (this.isPropertyMultiple(property))
				setter = (Operation) this.getStereotypeValue(property, Stereotypes.PROPERTY_MULTIPLE, "setter");
			if (o.equals(setter))
				return property;
		}
		return null;
	}
	
	public String getRemover(Class aClass, Operation operation) {
		return getAttributeForStereotypedOperation(aClass, Stereotypes.REMOVER, operation.getName());
	}
	
	public String getAttributeForStereotypedOperation(Class aClass, String stereotypeName, String operationName) {
		List<Property> properties = aClass.getOwnedAttributes();
		for (Property property: properties) {
			if (isProperty(property) || isPropertyMultiple(property)) {
				String propertyName = isProperty(property)? Stereotypes.PROPERTY : Stereotypes.PROPERTY_MULTIPLE;
				Operation value = (Operation) getStereotypeValue(property, propertyName, stereotypeName);
				if (value != null && value.getName().equals(operationName))
					return property.getName();
			}
		}
		return "";
	}
	
	public List<Property> getConstructorUninitializedAttributes(Operation constructor) throws Exception {
		//if (constructor.getAppliedStereotype("constructor") == null)
			//throw new Exception("Not a constructor operation.");
		
		Class owner = (Class) constructor.getOwner();
		List<Property> properties = new ArrayList<Property>();
		List<Property> initializedProperties = new ArrayList<Property>();
		
		String initializerStereotypeName = getStereotypeName(Profiles.CONNECTOR, Stereotypes.INITIALIZER);
		String initializerParentStereotypeName = getStereotypeName(Profiles.CONNECTOR, Stereotypes.INITIALIZER_PARENT);
		
		for (Parameter parameter: constructor.getOwnedParameters()) {
			Stereotype initializer = parameter.getAppliedStereotype(initializerStereotypeName);
			Stereotype initializerParent = parameter.getAppliedStereotype(initializerParentStereotypeName);
			
			Property initializedProperty = null;
			
			if (parameter.isStereotypeApplied(initializer))
				initializedProperty = (Property) parameter.getValue(initializer, "property");
			
			else if (parameter.isStereotypeApplied(initializerParent))
				initializedProperty = (Property) parameter.getValue(initializerParent, "property");
			
			initializedProperties.add(initializedProperty);
		}
		
		for (Property property: owner.getOwnedAttributes()) {
			if (! initializedProperties.contains(property))
				properties.add(property);
		}

		return properties;
	}
	
	public List<Operation> getUnimplementedOperations(Classifier aClass) {
		List<Operation> implementedOperations = new ArrayList<Operation>();
		List<Operation> unimplementedOperations = new ArrayList<Operation>();
		List<Classifier> generals = aClass.getGenerals();
		
		for (Interface anInterface: aClass.allRealizedInterfaces())
			unimplementedOperations.addAll(anInterface.getAllOperations());
		
		if (!generals.isEmpty()) {
			Classifier baseClass = generals.get(0);
			
			for (Interface anInterface: baseClass.allRealizedInterfaces())
				implementedOperations.addAll(anInterface.getAllOperations());
			
			implementedOperations.addAll(getUnimplementedOperations(baseClass)); // recursive call
		}
		
		unimplementedOperations.removeAll(implementedOperations);
	
		return unimplementedOperations;
	}
	
}
