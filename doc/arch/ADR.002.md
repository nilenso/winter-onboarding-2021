# Title
Using spec validations at the boundary layers (i.e handler and DB layers) only

# Context
In any application, data flow through multiple layers. In FMS as well, the data flows from the outside world using HTTP, to the handler, which then passes it to models, and models to the database layer.

At each of these layers, it is important to check the validity of data, keep the database in a consistent format, and avoid bugs in business logic. To do the same, multiple different approaches could be used.

- Earlier we did not add validations for data, which had some constraints added like the uniqueness of the license plate of the cab. 
Rather the exception was getting caught by a generic middleware.
- For functions at different layers we were not selecting valid keys, rather expecting the format is adhered which would silently fail or
generate exceptions while inserting in the database

To avoid the pitfalls of not validating data, we can have multiple approaches:-

1. Validation at the boundary layers of DB and handler could be done. Also, at the model layer, a wrapper over specs could be used, which extracts only those keys, which are present in the spec of the model to which the data is related.
2. We can also add spec validation at all the layers (i.e models and DB)

This will felicitate data validation at boundary layers, achieving the result which we want.
# Decision
It has been decided to move ahead with adding spec validation at boundary layers(first approach).

# Status
Accepted

# Consequences
Pros: It will help us to add an additional check on the validity of the data. We won't overdo validation, which would be an additional overhead to maintain in the longer run. The clarity in where to check for different types of validation.
Cons: We are losing on the dynamic nature of the language by adding rigidity
