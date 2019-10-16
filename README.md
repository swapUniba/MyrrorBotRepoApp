# MyrrorBotRepoApp

Il nucleo dell'app è la classe Chat.java che gestisce il flusso del dialogo,
tramite la classe innestata Background andiamo ad effettuare una richesta alla
pagina intentDetection sul server che ci fornirà la risposta; la risposta viene 
analizzata come un JSONObject e in base al valore di intentName andremo a stabilire
qual è l'intent corretto con una serie di if a cascata.
La visualizzazione dei dati è basata sulla classe ConversationRecyclerView tramite 
questa classe verrà instanziata la nostra RecyclerView.
ConversationRecyclerView  utilizza dei vari viewHolder che sono stati definiti
per ogni tipo di risposta es.HolderMeteo HolderMe per le domande dell'utente ecc. 
ConversationRecyclerView è basata su una lista di ChatData questa classe contiene 
tutti i parametri dei vari holder che abbiamo definito e tramite il parametro type
è in grado di distinguere che holder usare, per ogni holder viene definito un metodo
configureViewHolderX (con X numero del parametro type) che viene inserito
nel metodo onBindViewHolder.
Nella classe Chat quindi quando viene definito un nuovo elemento da inserire nella recyclerView 
si deve instanziare un oggetto di tipo ChatData e settare correttamente il 
parametro type oltre a tutti i parametri che serviranno al relativo holder 
nella classe ConversationRecyclerView.
