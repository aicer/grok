### Outstanding Tasks ###


Loading default dictionary using class loader from classpath (make sure built-in patterns are in the CP)
Loading additional dictionaries (if available and specified)
Compiling pattern

Executing example strings against patterns for data extraction

For Flume, GrokInterceptor, extracted data will be injected into headers
One expression per header field/body compiled and stored in maps for execution later against incoming values
  
Adding a means to override existing files globally or locally (per Grok entry)
To minimize runtime branching and selection within loops, create dedicated handlers where overides are allowed and another handler where field overrides are not needed.


Need to add more built-in patterns also supported by Logstash

Rough Draft of GrokInterceptor Config
```

# Global Configuration for Interceptor
grok.override = false
grok.patterns_dir = /home/user/grok/dictionaries

# Configuration for Individual Expressions
grok.entries.0.source = @body
grok.entries.0.expression = %{INT:year} Copy Data from Integery
grok.entries.0.override = true

grok.entries.1.source = dataOrigin
grok.entries.1.expression = %{IP:ip_address} Rest of the Data
grok.entries.1.override = false

grok.entries.2.source = timeField
grok.entries.2.expression = %{DATETIME:original_datetime} Rest of the Data
grok.entries.2.override = true

```

The built-in dictionaries will be loaded, then any custom dictionary if specified will be loaded as well

Then the dictionary will be finalized by invoking the bind() method on the GrokDictionary object.

Inside the Code we need a List of GrokHandlers

These objects in the ArrayList will process the contents of the source fields (if present) against the compiled expressions for those source fields. @body means that the source is the body of the Event object.

Discovered groups will be injected into the event (if not already present in the event)

If overrides are allowed then no checks are done, the groups discovered will simply be injected into the event.

There will be a global and local setting for overrides.

Overrides for discovered groups in expressions can be enabled globally or locally. By default, they are disabled both globally and locally.


