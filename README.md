# vertx-eventbus-reply-error
A simple example of the vertx event bus reply error handler being called on a successful request


Producing the error:

Get request

localhost:40001/hello?name=bob

Get Response

Hello bob! How are you


System output:

Successfully deployed verticle: fc438c53-e4fb-4a1b-a792-92a79f55bc9d
Server listening on port 40001
Successfully deployed verticle: f9c18fb4-d404-4e78-9dea-b3a73a0e5be6
Successfully deployed all verticles

***We failed answered: Timed out after waiting 30000(ms) for a reply. address: 2***

