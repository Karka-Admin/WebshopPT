# Programavimo technologijų kursinis darbas
## Aprašymas
**Tema**: internetinė plaukų priemonių parduotuvė.

### Naudotos technologijos
- _Java_
- _JavaFX21_
- _openJDK21_
- _Bootstrap_
- _Maven_
- _Intellij IDEA_
- _Gluon Scene Builder_
- _Lombok_
- _SqlConnectorJ_
- _XAMPP_
- _MySQL_
- _Github_
- _Springboot_

## TODO
### Bendri reikalavimai
Kuriama parduotuvė, prie šios kuriamos sistemos jungiasi vartotojai. Vartotojai yra kelių tipų, klientai gali registruotis patys, darbuotojus registruoja administratoriai. Galima peržiūrėti prekes, jas pirkti, atšaukti užsakymą, priskirti atsakingus vadybininkus, patvirtinti užsakymą, valdyti prekes, vartotojus ir t.t. Prie prekių ir užsakymo galima palikti komentarus, komentarai prie prekių yra neribojamo gylio, galima palikti įvertinimą. Prekės saugomos sandėliuose/parduotuvėse, jų gali būti keli. Sistemos realizavimui keliami pagrindiniai reikalavimai:

- [X] Sistema turi vartotojus, kurie prisijungdami prie sistemos autentifikuojasi ir pasiekia tik jiems skirtus duomenis (Pavyzdžiui, klientai negali matyti sistemos vartotojų, tačiau mato savo užsakymus, prekes. Darbuotojai, mato visus vartotojus, bet negali jų keisti, tik savo duomenis. Darbuotojai skirtingų lygių ir skirtingas funkcijas gali matyti ir atlikti ir pan.)
- [X] Vartotojai yra bent 2 tipų – klientai ir parduotuvės darbuotojai (būtina išsaugoti vardą, pavardę, kontaktinę informaciją, gimimo datą ir t.t.). Tiek klientai, tiek darbuotojai turi požymių bendrų abiem, tačiau ir kažkuo skiriasi. Jungiamasi su prisijungimo vardu ir slaptažodžiu, slaptažodis duomenų bazėje turi būti hashed
- [X] Pirkėjai renkasi prekes iš galimų prekių sąrašo, palieka įvertinimus ir komentarus apie prekes, gali parašyti prie užsakymo. Peržiūri savo pirkinių istoriją, gali atšaukti vykdomus užsakymus, apmokėti užsakymą ir pan
- [X] Visas redagavimo teises turi administratoriai (kaip realizuosite, Jūsų pasirinkimas)
- [X] Parduotuvės darbuotojai pilnai valdo užsakymus (negali tik ištrinti jau įvykdytų ar vykdomų užsakymų), pilnai valdo prekes bei informaciją apie parduotuves. Darbuotojai priskiria atsakingą už užsakymą darbuotoją, jį galima pakeisti. Visi nepriskirti užsakymai yra matomi visiems darbuotojams ir jie gali “pasiimti” užsakymą. Jei užsakymo nepasiima niekas per vieną darbo dieną, jis turi būti iškeltas ir pažymėtas kaip skubus, kad jį kuo greičiau pamatytų
- [X] Tiek klientai, tiek pardavėjai gali rašyti ir atsakyti į kitus komentarus prie prekių. Prie užsakymo yra paprasta susirašinėjimo galimybė
- [X] Komentarų gylis prie prekių yra neribojamas
- [X] Galima filtruoti užsakymus pagal sukūrimo datas (intervale kažkokiame), statusus, klientus ir pan. Pasirinkite bent 3 požymius pagal ką filtruosite
- [X] Sistemą turi sudaryti dvi posistemės: darbalaukio ir internetinė sistema (web server)

### Grafinė vartotojo sąsaja
- [X] Remiantis kursinio darbo užduotimis, suprojektuoti sistemos klasių diagramą, kuri atitiktų objektinio programavimo principus ir leistų paprastai kurti ir valdyti visus projekto objektus
- [X] Aprašyti visas suprojektuotas klases, su tinkamai parinktais klasių kintamaisiais, jų tipais. Turi būti nemažiau kaip 2 tarpusavyje besisiejančios (pageidautinas kompozicijos ar agregacijos ryšys) klasės
- [X] Kiekvienai iš sukurtų klasių reikia realizuoti CRUD (C – create, naujo objekto kūrimas ar sudedamojo objekto pridėjimas; R – read, duomenų apie objektą gavimas; U – update, objekto duomenų atnaujinimas; D – delete, objekto šalinimas (ne tiek objekto naikinimas, kiek jį sudarančių kitų objektų šalinimas)) funkcijas. Vartotojui turi būti leidžiama valdyti (vykdyti CRUD funckijas) su visais sistemos objektais.
- [X] Reikia sukurti grafinę vartotojo sąsają. Grafinės sąsajos išvaizda gali būti kokia norite, bet turėtų būti panaudoti šie komponentai:
    - [X] Vienas pagrindinis langas ir bent po 1 iššokantį (perspėjimo ar klaidos pranešimą, spalvos ar failo pasirinkimo dialogą) ir 1 papildomą langą
    - [X] Panaudotas meniu arba tab'ų juosta, atskirų sistemos dalių valdymui
    - [X] Naudojami standartiniai elementai, tokie kaip duomenų įvedimo laukai, mygtukai ir pan
    - [X] Panaudoti sudėtingesni grafiniai elementai, tokie kaip lentelė, pasirinkimų sąrašas (su sugeneruotomis iš turimų duomenų reikšmėmis, o ne statinėmis) ir pan
    - [X] Realizuota duomenų įvedimo kontrolė ir klaidų pranešimai
    - [X] CRUD funkcionalumas grafinėje vartotojo sąsajoje

### Duomenų bazės
- [X] Prisijungimas prie duomenų bazės ir atsijungimas nuo jos
- [X] Duomenų gavimas iš duomenų bazės (tik pilno, tiek ir filtruoto, atrinkto)
- [X] Duomenų įrašymas į duomenų bazę
- [X] Duomenų šalinimas iš duomenų bazės
- [X] Duomenų redagavimas duomenų bazėje

### Web serviso kūrimas
- [X] Web serviso veikimas (galima kreiptis ir gauti atsakymą)
- [X] Duomenų gavimas html, json ar kitu formatu
- [X] Parametrų, duomenų perdavimas į serverį
- [X] Savo ankstesnių laboratorinių darbų panaudojimas sistemos funkcionalumo perkėlimui į web

## DOKUMENTACIJA
### Use-Case Diagrama
![Use-Case Diagram](PT_USE_CASE_DIAGRAM.svg)

### Class Diagrama
![Class Diagrama](PT_CLASS_DIAGRAM.svg)