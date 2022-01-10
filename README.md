# CoinbonLe
Ce Projet à été build sur une machine sur Windows.

Pour ce test j'ai utilisé les libraries suivante:

- **[Uniflow](https://github.com/uniflow-kt/uniflow-kt)** - C'est la librarie d'architecture de présentation que j'ai utilisée dans ma dernière mission pour mettre en place du MVI, ça m'a facilité la mise en place de celle-ci étant donné que je connais comment fonctionnent les cas spéciaux comme la gestion des erreurs mais on peut très bien utiliser une implémentation faite main pour ce genre de petit projet ou plus lourd comme [Mavericks](https://github.com/airbnb/mavericks).
- **[Epoxy](https://github.com/airbnb/epoxy)** - Idem que Uniflow, j'ai utilisé cette librairie récemment et elle était très utile lors de la composition de liste de composants dans un écran. C'est limite overkill de l'utiliser pour ce projet mais elle m'évite de devoir faire la configuration du recyclerview (clipping/optimisation en fonctionne de la taille des items), de créer un adapter avec un viewholder pour l'item d'album en combinant le tout avec une gestion de diffing.
- **[Store](https://github.com/dropbox/Store)** - En lisant le sujet j'ai vu 2 options pour permettre à l'application de garder en cache les albums et répondre à la problématique de performance. **Store** que j'ai souvent utilisé et qui m'évite de devoir gérer le fonctionnement d'un cache mémoire et d'orchestrer son invalidation, la récupération des nouvelles données depuis le server et son stockage dans la base de données. **Paging 3** de de Jetpack que je n'avais jamais utilisé mais semble répondre à la même problématique, cependant j'avais déjà travaillé avec _Paging 2_ et j'avais rencontré des problèmes lorsque j'avais tenté d'écrire des tests avec donc je suis partie avec **Store**. **Store** a rencontré clairemnt sa limite lorsque j'ai dû ajouter de la pagination dans l'application devant la masse d'albums à afficher ce qui fait que mon implémentation dans ce projet n'est pas fonctionelle et si je devais recommencer je partirais directement avec **Paging 3** surtout avec l'intégration dont celle-ci bénéficie avec d'autres librairie comme _Epoxy_ et _Room_.
- **[Room](https://developer.android.com/training/data-storage/room)** J'ai l'habitude de travailler aussi avec Room mais j'aurai très bien utilisé Realm ou SqlDelight mais les problèmes de threading de Realm me bloquent un peu et je n'ai jamais utilisé SqlDelight.
- **[Retrofit](https://square.github.io/retrofit/)** Une implementation avec uniquement du Okhttp aurait très bien pu être faite mais l'intégration avec Moshi pour parser le Json facilite grandement la tâche.
- **[Moshi](https://github.com/square/moshi)** J'ai l'habitude de l'utiliser avec sa bonne intégration avec Kotlin, surtout en complément avec [MoshiX](https://github.com/ZacSweers/MoshiX) pour obtenir des sealed class depuis du Json.