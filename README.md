# FF Homework

### Spusteni
Neni treba zadna externi konfigurace, napr. databaze. Pro jednoduchost se pouziva bundled H2 databaze, ktera si pro perzistenci mezi spustenimi aplikace vytvori v aktualnim adresari slozku 'db' s databazovymi soubory. 

### Poznamky k zadani
* zadani bylo v cestine, tak pro dokumentaci pouziji take cestinu (kod v AJ)
* H2 DB jsem zvolil, protoze to je default ve spring boot a pro tento ukol to nevnimam jako podstatne

### Poznamky k implementaci
* Dilema jestli aplikaci napsat minimalistickym zpusobem, ktery bude zkratka pouze funkcni nebo robustnejsim zpusobem, aby byla aplikace pripravena pro budouci rozsireni jsem vyresil tak, ze jsem zvolil robustnejsi reseni. Duvody byly jiz zminena pripravenost pro budouci rozsireni a zaroven vetsi podobnost s jinymi pripadnymi komplexnimi aplikacemi nasledujici stejne konvence.
* Lakalo me to napsat pomoci SPRING DATA REST, ktery se zrejme jevi jako idealni kandidat. Avsak zatim jsem to nikdy prakticky nepouzil a mohlo by tak dojit k situaci, ze narazim na nejaka omezeni, ktera snizi flexibilitu vysledne funkcionality nebo ktera budou vyzadovat delsi cas na vyreseni. Zvolil jsem tedy sazku na jistotu a i v realne praxi bych pri sibenicnim terminu neriskoval s necim novym. Urcite ale jedna z prvnich veci na TODO listu bude zkusit to naimplementovat prave pomoci SPRING DATA REST. Treba to bude idealni volba pro tento usecase a moje obavy (napr. ze slozeneho klice a jeho reprezentace jako /frameworkName/version) se ukazou jako liche.  
* V repository pouzivam record pro projekci, protoze zastavam defenzivni pristup, ze s entitama naprimo se ma pracovat pouze pokud s nimi hodlam nejak manipulovat. Pro 'view' by se mela vracet projekce. V teto trivialni aplikaci to neni tak dulezite a muze to byt vnimano jako overhead. Nemam obecne rad, kdyz jsou entity pouzivany napric aplikaci. Nezkuseny programator s nimi pak muze napachat docela neplechu.
* 4xx a 5xx odpovedi nevraci detaily; jejich implementace by uz zavisela na konkretnim klientovi
* Vhledem k tomu, ze z pohledu Rest Api pouzivam jako identifikator kombinaci nazvu JS frameworku a verze a vysledne URI je tak vlastne urcovano volajicim klientem (jinymi slovy resource identifikator neni generovan aplikaci samotnou), tak jsem si dovolil vypustit POST pro vytvoreni noveho zaznamu a vyuzil jsem PUT pro vytvoreni i aktualizaci zaznamu.
* Pro zjednoduseni implementace je nazev js frameworku v ramci URI non case sensitive. Tudiz ../React a ../react jsou stejnym resource.  
* Vzhledem k poctu trid jsou vsechny v jednom package. Interfaces jsou kvuli jednoduchosti take vynechany.
* Testy by se daly dale rozsirovat. Vzhledem k casu jsem se zameril, abych mel hlavne pokryte hlavni funkcionality a mel tak jistotu, ze rozsirovanim a upravami jsem nic nerozbil.
* Za me je to ted ve stavu, ze to splnuje zadani a je to funkcni tak, ze by to melo obstat v provozu. Samozrejme je stale prostor at pro zlepseni nebo doladeni souvisejici s realnym klientem a zpusobem nasazeni, ale to jsou veci, ktere by se normalne resily v prubehu dotazovanim/upresnovanim. Jelikoz je ale hlavne dulezita funkcnost, tak to jsou uz detaily.  
* Snad toho alibismu neni moc :-)

### Mozna dalsi rozsireni
* vetsi pokryti vsech moznych scenaru testy a dat jim nejakou stabni kulturu
* vracet podrobnosti k jinym nez 2XX odpovedim; logovat validacni chyby (to by byla asi prvni vec, kterou bych nyni pridal)
* https
* zlepseni strankovani
* hateoas
* verzovani api
* a jine drobnejsi veci...