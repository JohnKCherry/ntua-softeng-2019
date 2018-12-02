# ntua-softeng-2019

Μέλη της ομάδας (Conqu-ERRORS)

Κερασιώτης Ιωάννης - Α.Μ. 03114951  
Κεφάλας Γιώργος   - Α.Μ. 03114119  
Ραφτόπουλος Ευάγγελος - Α.Μ. 03114743  
Χριστοδουλόπουλος Δανιήλ - Α.Μ. 03114797  
Θηβαίος Αναστάσιος  - Α.Μ. 03114785  
Τσόπελας Κωνσταντίνος - Α.Μ. 03114441  






## Basic Git Workflow

### Disclaimer
Γενικά εάν δεν είστε σίγουροι τι κάνετε ή έχετε απορίες ρωτήστε στο discord ή το [Google](https://www.google.com). Ούτε εγώ είμαι 100% σίγουρος για τα παρακάτω δεν έχω δουλέψει πότε σε ομαδικό project με το git οπότε εάν κάνω κάποιο λάθος μπορείτε να γράψετε τις δικές σας συμβουλές-οδηγίες εδώ.

### Έχουμε και λέμε...
Γενικά προτείνω να εργάζεται ο καθένας στο δικό του branch για να μην
"πυροβολάμε" 🔫 τα πόδια ο ένας του άλλου. Ο τρόπος είναι ο εξής: 

Υποθέτοντας οτι όλοι έχουμε κάνει clone το repository τοπικά στο pc πηγαίνουμε
σ εκείνο το directory. Την __πρώτη__ φορα και μόνο θα πρέπει ο καθένας να
δημιουργήσει ένα branch. 

Με την εντολή `git branch <Name-Of-Branch>` δημιούργουμε ενα branch.

Πχ
```sh
$ git branch daniel
```
δημιούργησα ένα branch με το όνομα "daniel".

Άν δώσουμε τώρα 

```sh
$ git branch
```
Θα φαίνεται το εξής:

```console  
  daniel
* master
```
και πιθανόν και άλλα branches που δημιουργήσαμε. Το * σημαίνει σε πιο branch
βρίσκεσαι τώρα. Για να πας στο δικό σου branch για να δουλέψεις θα κάνεις `git
checkout <Name-Of-Branch>`.

Πχ 
```sh
$ git checkout daniel
```

Άρα αυτό που θα κάνεις κάθε φόρα που θέλεις να δουλέψεις στο project είναι να
ελέγχεις αν είσαι στο δικό σου branch και __όχι__ στο master και θα κάνεις τα
κόλπα σου εκεί 😉.

### Add & Commit
Τώρα ας πούμε ότι δημιουργείς ένα αρχείο με όνομα home.html το ανοίγεις να κάνεις τα κόλπα και έχεις ολοκληρώσει αυτό που ήθελες να κάνεις. Αυτό που πρέπει να κάνεις τώρα είναι το εξής. 
```sh
$ git add home.html
$ git commit -m "Home page done"
```
που σημαίνει, πρόσθεσε τις αλλαγές μου και κάνε commit μαζί με μια μικρή
περιγραφή το τι αλλαγές έκανες. Τώρα οι αλλαγές βρίσκονται στο δικό σου branch.
### Push
Για να ανανεώσεις το repository στο github θα πρέπει να δώσεις 

```sh
$ git push origin daniel
```
όπου "daniel" βάλε το δικό σου branch.

Αυτό θα κάνει push τις αλλαγές στο branch με όνομα daniel __όχι__ στο master.

### Merge master

Κάποια στιγμή όταν θα έχεις τελειώσεις με αυτό που κάνεις και είσαι σίγουρος οτι δουλεύει μπορείς να ανανεώσεις το master branch με τις αλλαγές σου. Για να το κάνεις αυτό ακολουθείς την εξής διαδικασία.

Πηγαίνουμε στο master branch.
```sh
$ git checkout master 
```
Ανανεώνουμε το τοπικό master branch (δηλ. το master στο pc σου) με τις όποιες πιθανόν αλλαγές έκανε κάποιος συνεργάτης μας.

```sh
$ git pull
```
Συγχωνεύουμε τις δικές μας αλλαγές από το branch που μας αντιστοιχεί. Πχ το δικό μου είναι "daniel".

```sh
$ git merge daniel
```
Και ανανεώνουμε το repository στο github

```sh
$ git push origin master
```

### Ανανέωση του branch
Γενικά εάν θέλουμε να ανανεώσουμε το τοπικό μας repository (δηλ. στο pc μας) με τις αλλαγές που κάνανε οι συνεργάτες κάνουμε τα εξής.

Πηγαίνουμε στο master.

```sh
$ git checkout master
```
και κάνουμε pull τις αλλαγές

```sh
$ git pull
```

### Συγχώνευση αλλαγών στο δικό μου branch 
Και εαν θέλουμε να ανανεώσουμε το δικό μας branch με τις καινούργιες αλλαγές που πιθανόν να τις χρειαζόμαστε...

Μετάβαση στο branch που μας αντιστοιχεί
```sh
$ git checkout daniel
```
fetch των αλλαγών στα branches

```sh
$ git fetch
```

και συγχώνευση με το master

```sh
$ git rebase master
```

Το rebase και το master κάνουν διαφορετική δουλεία με παρόμοιο αποτέλεσμα, δηλαδή και τα δυο κάνουν συγχώνευση.

### Μια ξανά...

Άρα έχουμε και λέμε... Όταν θα αποφασίζετε να δουλέψετε κάντε πρώτα ένα `git pull` στο master branch να δείτε τι αλλαγές κάνανε οι άλλοι. Έαν χρειάζεστε τις αλλαγές στο δικό σας branch κάντε [Συγχώνευση αλλαγών στο δικό μου branch](#Συγχώνευση-αλλαγών-στο-δικό-μου-branch). Μετά κάντε τις δικές σας αλλαγές στο δικό σας branch. Έπειτα [add & commit](#add--commit), ενημέρωση του δικόυ σας branch με [push](#Push). Και όταν είστε έτοιμοι [merge με το master](#Merge-master).

### Παράδειγμα
```sh
$ git checkout daniel
$ vim home.html
# ή όποιον text editor/ide θέλετε
# Τελείωσα με τις αλλαγές
$ git add home.html
$ git commit -m "Home page done"
$ git push origin daniel
# Το πρόγραμμα μου δεν εχει bugs είμαι γαμάτος άρα τα συγχωνεύω στο master
$ git checkout master
$ git pull
$ git merge daniel
# Δώσε τις αλλαγές στο github
$ git push origin master
# πηγαίνε για ύπνo
$ shutdown now 
