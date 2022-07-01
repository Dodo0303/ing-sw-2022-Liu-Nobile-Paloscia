# SUPPLEMENTARY NOTES
- Clients cannot update their local copy of the model until they receive an ACK from the server that approves a certain 
action. This guarantees consistency of the models during the match. Messages sent from server instead do not expect an ACK. 
Each ACK is broadcasted to all clients of that match (except for the NickResponseMessage, which is not broadcasted).
-  In the class diagram all ACKs triggered by the use of the characters are missing. This was intentional, as they are 
pretty similar to their corresponding messages sent from the client.
-  According to the game rules, a player may use a character card at any point during his turn. Nevertheless, we believe 
that some specific sequences of actions should be considered atomic. For instance, selecting a student to move and choosing 
a character before actually moving it seems like a senseless scenario to us. In order to make the sequence diagrams more 
readable and less repetitive, we decided to put an “opt CHARACTER” frame every time the player is allowed to choose a 
character.

