package root.iv.ivandroidacademy.data

import root.iv.ivandroidacademy.data.model.Actor
import root.iv.ivandroidacademy.data.model.Movie

object DataRepository {
    
    val actors = listOf(
        Actor(
            id = 1,
            name = "Robert Downey Jr.",
            photoUrl = "https://static.wikia.nocookie.net/snl/images/e/e3/Robert_Downey_Jr..jpg/revision/latest/top-crop/width/360/height/450?cb=20200725224545",
        ),
        Actor(
            id = 2,
            name = "Chris Evans",
            photoUrl = "https://www.film.ru/sites/default/files/people/1459396-1176887.jpg",
        ),
        Actor(
            id = 3,
            name = "Mark Ruffalo",
            photoUrl = "https://www.film.ru/sites/default/files/people/1457294-1182298.jpg",
        ),
        Actor(
            id = 4,
            name = "Chris Hemsworth",
            photoUrl = "https://cdn.britannica.com/92/215392-050-96A4BC1D/Australian-actor-Chris-Hemsworth-2019.jpg",
        ),
        Actor(
            id = 5,
            name = "Scarlett Johansson",
            photoUrl = "https://i.discogs.com/U6Fjw9wGGRpyU5Tz6z6m-RSVw2qu7BLPaaflR01jNYg/rs:fit/g:sm/q:40/h:300/w:300/czM6Ly9kaXNjb2dz/LWRhdGFiYXNlLWlt/YWdlcy9BLTExMTI5/NzctMTUzNDY2Mzk3/Ny0yNTMzLmpwZWc.jpeg"
        ),
        Actor(
            id = 6,
            name = "Kate Beckinsale",
            photoUrl = "https://www.film.ru/sites/default/files/people/1455694-1661736.jpg"
        )
    )
    
    val movies = listOf(
        Movie(
            id = 1,
            title = "Avengers: End Game",
            tags = "Action, Adventure, Drama",
            duration = 137,
            pg = "13+",
            rating = 4f,
            reviewsCount = 125,
            poster = "https://www.pinkvilla.com/imageresize/avengers-endgame-poster_1.jpg?width=752&format=webp&t=pvorg",
            poster2 = "https://www.pinkvilla.com/imageresize/avengers-endgame-poster_1.jpg?width=752&format=webp&t=pvorg",
            isLike = false,
            storyLine = "The main character is a secret agent who passes a cruel test of reliability and joins an incredible mission. The fate of the world depends on its implementation, and for success it is necessary to discard all previous ideas about space and time.",
            castIds = listOf(1, 2, 3, 4, 5, 6),
        ),
        Movie(
            id = 2,
            title = "Tenet",
            tags = "Action, Sci-Fi, Thriller",
            duration = 97,
            pg = "16+",
            rating = 5f,
            reviewsCount = 98,
            poster = "https://media.kg-portal.ru/movies/t/tenet/fanart/tenet_6.jpg",
            poster2 = "https://media.kg-portal.ru/movies/t/tenet/fanart/tenet_6.jpg",
            isLike = true,
            storyLine = "After the devastating events of Avengers: Infinity War, the universe is in ruins. With the help of remaining allies, the Avengers assemble once more in order to reverse Thanos' actions and restore balance to the universe.",
            castIds = listOf(5, 6, 1),
        ),
        Movie(
            id = 3,
            title = "Black Widow",
            tags = "Action, Adventure, Sci-Fi",
            duration = 102,
            pg = "13+",
            rating = 4f,
            reviewsCount = 1,
            poster = "https://media.kg-portal.ru/movies/b/blackwidow/posters/blackwidow_32.jpg",
            poster2 = "https://media.kg-portal.ru/movies/b/blackwidow/posters/blackwidow_32.jpg",
            isLike = false,
            storyLine = "After the devastating events of Avengers: Infinity War, the universe is in ruins. With the help of remaining allies, the Avengers assemble once more in order to reverse Thanos' actions and restore balance to the universe.",
            castIds = listOf(1, 6, 4),
        ),
        Movie(
            id = 4,
            title = "Wonder Woman 1984",
            tags = "Action, Adventure, Fantasy",
            duration = 120,
            pg = "13+",
            rating = 5f,
            reviewsCount = 74,
            poster = "https://upload.wikimedia.org/wikipedia/ru/archive/6/67/20210918062207%21Wonder_Woman_1984_%28poster%29.jpg",
            poster2 = "https://upload.wikimedia.org/wikipedia/ru/archive/6/67/20210918062207%21Wonder_Woman_1984_%28poster%29.jpg",
            isLike = false,
            storyLine = "Continuation of the fantastic exploits of the Amazon warrior Diana, who left her secluded island to save humanity. After overcoming the tragedy of World War I, Wonder Woman is embroiled in a new international battle of the mid-1980s.",
            castIds = listOf(3, 2, 1),
        ),
        Movie(
            id = 5,
            title = "Iron Man 2",
            tags = "Action, Adventure, Fantasy",
            duration = 125,
            pg = "13+",
            rating = 5f,
            reviewsCount = 174,
            poster = "https://i.ebayimg.com/images/g/rxYAAMXQUY1Q9tLb/s-l500.jpg",
            poster2 = "https://i.ebayimg.com/images/g/rxYAAMXQUY1Q9tLb/s-l500.jpg",
            isLike = false,
            storyLine = "After the devastating events of Avengers: Infinity War, the universe is in ruins. With the help of remaining allies, the Avengers assemble once more in order to reverse Thanos' actions and restore balance to the universe.",
            castIds = listOf(1, 5),
        ),
        Movie(
            id = 6,
            title = "Sherlock Holmes",
            tags = "Action, Adventure, Fantasy",
            duration = 100,
            pg = "13+",
            rating = 5f,
            reviewsCount = 115,
            poster = "https://m.media-amazon.com/images/I/61f2eN+A2sL._AC_.jpg",
            poster2 = "https://m.media-amazon.com/images/I/61f2eN+A2sL._AC_.jpg",
            isLike = false,
            storyLine = "After the devastating events of Avengers: Infinity War, the universe is in ruins. With the help of remaining allies, the Avengers assemble once more in order to reverse Thanos' actions and restore balance to the universe.",
            castIds = listOf(1, 5),
        ),
        Movie(
            id = 7,
            title = "The Old Guard",
            tags = "Action, Sci-Fi, Thriller",
            duration = 84,
            pg = "16+",
            rating = 3f,
            reviewsCount = 115,
            poster = "https://upload.wikimedia.org/wikipedia/ru/archive/e/e1/20200713120115%21The_Old_Guard_%282020_film%29.jpg",
            poster2 = "https://upload.wikimedia.org/wikipedia/ru/archive/e/e1/20200713120115%21The_Old_Guard_%282020_film%29.jpg",
            isLike = false,
            storyLine = "\"The Old Guard\" is the story of old soldiers who never die, and yet cannot seem to fade away. Trapped in an immortality without explanation, Andromache of Scythia 'Andy' and her comrades ply their trade for those who can find-and afford-their services.",
            castIds = listOf(2, 5, 6),
        )
    )
}