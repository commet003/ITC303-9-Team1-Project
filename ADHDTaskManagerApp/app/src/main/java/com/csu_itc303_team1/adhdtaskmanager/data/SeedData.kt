package com.csu_itc303_team1.adhdtaskmanager.data

import java.time.Duration
import java.time.Instant

object SeedData {

    val Users = listOf(
        User(id = 1L, username = "User Profile Picture", avatar = Avatar.USER_PROFILE_PICTURE),
        User(id = 2L, username = "Default User Picture", avatar = Avatar.DEFAULT_USER),
        User(id = 3L, username = "Default User Picture 2", avatar = Avatar.DEFAULT_USER)
    )

    val Tags = listOf(
        Tag(id = 1L, label = "2.3 release", color = TagColor.BLUE),
        Tag(id = 2L, label = "2.4 release", color = TagColor.RED),
        Tag(id = 3L, label = "a11y", color = TagColor.GREEN),
        Tag(id = 4L, label = "UI/UX", color = TagColor.PURPLE),
        Tag(id = 5L, label = "eng", color = TagColor.TEAL),
        Tag(id = 6L, label ="VisD", color = TagColor.YELLOW),
    )

    val Todos = arrayListOf(
        Todo(
            id = 1L,
            title = "New mocks for tablet",
            description = "For our mobile apps, we currently only target phones. We need to " +
                    "start moving towards supporting tablets as well, and we should kick off the " +
                    "process of getting the mocks together",
            status = TodoStatus.NOT_STARTED,
            creatorId = 2L,
            orderInCategory = 1
        ),
        Todo(id = 2L,
            title = "Move the owner icon",
            description = "Our UX research has shown that users prefer that, and even though " +
                    "all us remain split on this, let’s go ahead and move the owner icon in the " +
                    "way specified in the mocks.",
            status = TodoStatus.NOT_STARTED,
            creatorId = 3L,
            dueAt = Instant.now() - Duration.ofDays(1),
            orderInCategory = 2
        ),
        Todo(
            id = 3L,
            title = "Allow optional guests",
            description = "Feedback from product: people find our app useful for scheduling " +
                    "things, but they really, really want to have a way to have optional " +
                    "invites. Right now, anyone who receives a calendar invite has no way of " +
                    "knowing how important (or unimportant) their presence is at the meeting. " +
                    "Alternatively, we could set up some kind of numerical 'important to attend' " +
                    "system (with 5 as most important, and 1 as totally optional), but this " +
                    "might be needlessly complex. Adding a simple “optional” bit in db might " +
                    "be all we need (plus the necessary UI changes). \n",
            status = TodoStatus.NOT_STARTED,
            creatorId = 1L,
            createdAt = Instant.now() - Duration.ofDays(1),
            dueAt = Instant.now() - Duration.ofDays(2),
            orderInCategory = 3
        ),
        Todo(
            id = 4L,
            title = "Suggest meeting times",
            description = "I’m finding that I have to look at the invitees’ calendars and work " +
                    "out the optimal time for a meeting. Surely our app can work this out and " +
                    "just offer a few times as the guest list is created? I think this could save " +
                    "our users a good amount of time. ",
            status = TodoStatus.NOT_STARTED,
            creatorId = 2L,
            dueAt = Instant.now() + Duration.ofDays(5),
            orderInCategory = 4
        ),
        Todo(
            id = 5L,
            title = "Enable share feature",
            description = "We’ve talked about this feature for a while and it’s fairly easy to " +
                    "implement (and the mocks exist). Let’s get it in the next release \n",
            status = TodoStatus.NOT_STARTED,
            creatorId = 3L,
            dueAt = Instant.now() - Duration.ofDays(10),
            orderInCategory = 5
        ),

        Todo(
            id = 6L,
            title = "Support display modes",
            description = "Default to the weekly view in desktop and daily view in mobile , but " +
                    "let users switch between modes seamlessly",
            status = TodoStatus.IN_PROGRESS,
            creatorId = 2L,
            dueAt = Instant.now() + Duration.ofDays(2),
            orderInCategory = 1
        ),
        Todo(
            id = 7L,
            title = "Let users set bg color",
            description = "We may want to present a finite palette of colors from which the user " +
                    "chooses the default; if there are a large number of options, we’ll probably " +
                    "find ourselves dealing with poor contrast between foreground and background",
            status = TodoStatus.IN_PROGRESS,
            creatorId = 2L,
            dueAt = Instant.now() + Duration.ofDays(12),
            orderInCategory = 2
        ),
        Todo(
            id = 8L,
            title = "Implement smart sync",
            status = TodoStatus.COMPLETED,
            creatorId = 1L,
            dueAt = Instant.now() + Duration.ofDays(22),
            orderInCategory = 1
        ),
        Todo(
            id = 9L,
            title = "Smartwatch UI design",
            status = TodoStatus.COMPLETED,
            creatorId = 3L,
            dueAt = Instant.now() + Duration.ofDays(14),
            orderInCategory = 2
        ),
        Todo(
            id = 10L,
            title = "New content for notifications",
            description = "Not if types: event coming up in [x] mins; event right now; time to " +
                    "leave; event changed / cancelled; invited to new event",
            status = TodoStatus.COMPLETED,
            creatorId = 2L,
            dueAt = Instant.now() - Duration.ofDays(9),
            orderInCategory = 3
        ),
        Todo(
            id = 11L,
            title = "Create default illustrations for event types",
            description = "Event types: coffee, lunch, gym, sports, travel, doctors appointment, " +
                    "game day, presentation, movie, theatre, class",
            status = TodoStatus.COMPLETED,
            creatorId = 3L,
            dueAt = Instant.now() + Duration.ofDays(6),
            orderInCategory = 4
        ),
        Todo(
            id = 12L,
            title = "Auto-decline holiday events",
            description = "User setting that automatically declines new meetings if they’re set " +
                    "on a holiday",
            status = TodoStatus.COMPLETED,
            creatorId = 3L,
            dueAt = Instant.now() - Duration.ofDays(3),
            orderInCategory = 5
        ),
        Todo(
            id = 13L,
            title = "Holiday conflict warning",
            description = "Pop up dialog warning user if they try to schedule a meeting on at " +
                    "least one of the participants’ holiday, depending on user profile location",
            status = TodoStatus.COMPLETED,
            creatorId = 1L,
            dueAt = Instant.now() + Duration.ofDays(1),
            orderInCategory = 1,
            isArchived = true
        ),
        Todo(
            id = 14L,
            title = "Prepopulate holidays",
            description = "Show national holidays (listed at top of each day’s schedule) " +
                    "directly in calendar view based on user profile location. Let users opt out " +
                    "of this if they want",
            status = TodoStatus.COMPLETED,
            creatorId = 3L,
            dueAt = Instant.now() + Duration.ofDays(8),
            orderInCategory = 2,
            isArchived = true
        ),
        Todo(
            id = 15L,
            title = "Holiday-specific illustrations",
            description = "Create illustrations that match each holiday. Prioritize for tier 1 " +
                    "regions first",
            status = TodoStatus.COMPLETED,
            creatorId = 3L,
            orderInCategory = 3,
            isArchived = true
        ),
    )

    val TodoTags = listOf(
        TodoTag(todoId = 1L, tagId = 2L),
        TodoTag(todoId = 1L, tagId = 4L),

        TodoTag(todoId = 2L, tagId = 1L),

        TodoTag(todoId = 3L, tagId = 4L),
        TodoTag(todoId = 3L, tagId = 5L),

        TodoTag(todoId = 4L, tagId = 2L),
        TodoTag(todoId = 4L, tagId = 5L),

        TodoTag(todoId = 5L, tagId = 1L),
        TodoTag(todoId = 5L, tagId = 4L),

        TodoTag(todoId = 6L, tagId = 1L),
        TodoTag(todoId = 6L, tagId = 5L),
        TodoTag(todoId = 6L, tagId = 6L),

        TodoTag(todoId = 7L, tagId = 2L),
        TodoTag(todoId = 7L, tagId = 3L),

        TodoTag(todoId = 8L, tagId = 1L),

        TodoTag(todoId = 9L, tagId = 2L),

        TodoTag(todoId = 10L, tagId = 1L),
        TodoTag(todoId = 10L, tagId = 4L),

        TodoTag(todoId = 11L, tagId = 2L),

        TodoTag(todoId = 12L, tagId = 4L),
        TodoTag(todoId = 12L, tagId = 6L),

        TodoTag(todoId = 13L, tagId = 1L),
        TodoTag(todoId = 13L, tagId = 3L),
        TodoTag(todoId = 13L, tagId = 5L),
        TodoTag(todoId = 13L, tagId = 6L),

        TodoTag(todoId = 14L, tagId = 2L),

        TodoTag(todoId = 15L, tagId = 1L),
        TodoTag(todoId = 15L, tagId = 6L)
    )

    val UserTodos = listOf(
        UserTodo(userId = 1L, todoId = 1L)
    )
}
