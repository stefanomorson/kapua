# Metrics Configurable Indexing

## Description
We want to let the user define wich messages should be stored and within messages which metrics should
be indexed and wich not. 
- In some use cases users wants to store messages of certian topics, e.g  diagnostis and alert messages,
but the other telemetry meesages are routed to an external system via the Routes feature. 
- In some use cases messages have hundreds or even thousands of metrics. Users want to store them but
most of the times they don't need to index each and every metric but just few or simply only the date.
Currently the datastore indexes all the metrics by default, this behaviour can become costly when messages
carry hundreds or thousands of metrics and might giv eno value to the user.

## Analysis
Prerequisite: parameter datastore.schema.messages.mapping.metrics.dynamic is configuredo to false.

Each account can have a set of message rules and metric rules.
Message rules determine if an incoming message has to be stored or discarded. 
If discarded the datastore route should not consider this an error (like when the the datastore is disabled).
Rules are evaluated from the first to the last. Processing exits whith the first rule that matches the message.
The rule return an action and can optionally return a tag. If the action is STORE, the tag can be used to restrict 
the set of metric rules later evaluated for the message.

{
    "messageRules": [
        {
            "name": "",
            "description": "",
            "condition":"match(topic,'AABBCCDDEE/#')",
            "action": "DISCARD"
        },
        {
            "name": "",
            "description": "",
            "condition":"match(topic,'+/#')",
            "action": "STORE",
            "tag": "pippo"
        }
    ]
}

Metric rules determine if a metric of the message has to be just stored or stored and indexed.
Rules set is detemined by the tag returned by the message rules at the previous step. Rules in 
the set are evaluated from the first to the last. Processing exits whith the firs rule that match 
the message metric.

{
    "metricRules": [
       {
            "name": "",
            "description": "",
            "messageTags": ["pippo"],
            "condition":"match(metric.name,'docker_*'),eq(metric.type,'long'))",
            "action": "STORE"
        },
        {
            "name": "",
            "description": "",
            "messageTags": ["pippo"],
            "condition":"and(match(metric.name,'*'),match(metric.type,'*'))",
            "action": "STORE_AND_INDEX"
        }
    ],
}

If the metric should just be stored it will not be added to the index mappings. Thanks to the fact
that dynamic mapping is disabled, Elasticsearch will not try to create mappings automatically.

If a metric is not indexd it is still regitered in the metric registry but a new field `indexed` must 
be added in the metric registry entry so that the information can be used by the client applications.
Possibly add a field in the entry that logs when the the field changed its value. 
