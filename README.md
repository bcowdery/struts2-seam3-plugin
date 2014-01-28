Struts2 Seam3 Plugin
====================

Out of the box this plugin adds support for Seam3 extended conversation contexts, Seam3 Internationlization &
validation of Struts2 actions using JSR-303 bean validations.


# Configuration

## Web.xml

The seam3 plugin uses a servlet filter to bind conversation contexts to the request lifecycle. You'll need to 
update your web.xml file to include either the `WebBeansConversationFilter` for Apache WebBeans CDI implementations 
(TomEE) or the `WeldConversationFilter` for Weld (JBoss AS).

```
<!-- Conversation support for non-JSF applications -->
<filter>
    <filter-name>Seam Conversation Support</filter-name>
    <filter-class>com.pointyspoon.webbeans.filters.WebBeansConversationFilter</filter-class>
    <init-param>
        <param-name>conversationIdParam</param-name>
        <param-value>cid</param-name>
    </init-param>
</filter>

<filter-mapping>
    <filter-name>Seam Conversation Support</filter-name>
    <url-pattern>/*</url-pattern>
</filter-mapping>
```

The `conversationIdParam` init parameter tells the filter to monitor the HTTP request for parameter value denoting the
ID of a long running conversation. If this parameter is present the filter will re-activate the conversation for the
duration of the request. This parameter is optional and can be safely omitted from your web.xml file, defaults to `cid`.

## META-INF/beans.xml

As with most CDI containers, add an empty beans.xml file to your project to tell the CDI container to scan your project
for CDI beans.

```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://java.sun.com/xml/ns/javaee"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://docs.jboss.org/cdi/beans_1_0.xsd">

</beans>
```


# Conversations

### Injectable Conversation

The `Conversation` object is injectable and can be used to manage a running conversation.

```
@Inject Conversation conversation

public void begin() {
    if (conversation.isTransient())
        conversation.begin();
}

public void end() {
    conversation.end();
}
```


### Conversation Scoped Beans

Conversation scoped beans can be created to allow a unit of work to span multiple request cycles. Any named bean
annotated with `@ConversationScoped` will automatically be bound to conversation scope.

```
@Named
@ConversationScoped
public HelloWizard {

    private String name;

    public String getName() { return name; }
    public String setName(String name) { this.name = name; }
}
```

### Conversation ID tags & parameters

Long running conversations are bound to a conversation scope and assigned an ID. The Seam3 filters will restore
a long running conversation for any request that specifies conversation ID. When working with a conversation scope
ensure that your links and form submissions contain the conversation ID by using the provided `seam` tags.

`ConversationIdParamTag`:

```
<s:a namespace="/wizard" action="next">
    <seam:conversationparam/>
    Next page
</s:a>
```

<br/>


`ConversationIdFieldTag `:

```
<s:form id="save" action="save">
   <seam:conversationfield/>
   ...
</s:form>
```



# Seam Internationalization

Seam3 Internationlization is used in place of the default Struts2 localization. You can configure localization by 
creating a CDI producer that provides defaults for locale, timezone and the location of message bundles. 

```
public class InternationalDefaultsProducer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Produces
    @DefaultTimeZone
    private String defaultTimeZoneId = "America/Edmonton";  // GMT-7

    @Produces
    @DefaultLocale
    private String defaultLocaleKey = "en_US";              // American English

    @Produces
    @DefaultMessageBundle
    private String messagesBundleClasspath = "Messages";    // classes\Messages.properties
}
```

## Struts Action Support

Text localization is provided through the `SeamTextProviderSupport` class. This class provides an implementation
of the struts `getText` method that uses the Seam3 internationalization facilities for localization.

In most cases, you'll want to use the `SeamActionSupport` class for your struts2 actions as it provides additional
support for field validations, error messages and action messages that matches 

### Date Formatting

You can apply custom formatting to dates by using the `getText` method to retrieve a date format string:

```
<s:date name="#person.birthday" format="%{getText('pretty.date.format')}"/>
```


## UI Messages

The provided `UIMessages` class can be used to provide localized flash messages to the client. These messages are held
in a conversation scope until rendered using the `<seam:messages/>` tag.

* `UIMessages#info(String messageKey, Object... args)`
* `UIMessages#warn(String messageKey, Object... args)`
* `UIMessages#error(String messageKey, Object... args)`
* `UIMessages#fatal(String messageKey, Object... args)`


**Messages.properties**

```
signup.thanks=Thanks for signing up {0}! You can now log in as {1}.
```

**Action:**

```
@Inject UIMessages messages;

@Action("signup")
public String signup() {    
    messages.error("signup.thanks", "Bob", "username");    
    return INPUT;
}
```

**View:**

```
<seam:message cssClass="alert"/>
```



# Bean Validation

Struts2 actions extending from `SeamValidatorActionSupport` will perform validation of beans using JSR-303 bean
validation. As with struts2, validation messages will be added as field errors and can be rendered using standard
struts2 tags.

```
public @Model class Person {
    @Column @NotNull
    private String firstName;
    @Column @NotNull
    private String lastName;    
    @Column @Email
    private String email;

    // getters and setters
}

public class PersonAction extends SeamValidatorActionSupport {

   Long id = null;
   Person person;

   @Valid
   public Person getPerson() {
      return person;
   }
}
```

## Validation Messages

With most JSR-303 implementations you can provide custom validation messages by including a `ValidationMessages.properties` 
file on your classpath.

```
javax.constraints.Pattern.lettersonly=may only contain letters
javax.constraints.Pattern.postalcode=must be a valid postal code
javax.constraints.Phone=must be a valid phone number
javax.constraints.URL=must be a valid URL
```
