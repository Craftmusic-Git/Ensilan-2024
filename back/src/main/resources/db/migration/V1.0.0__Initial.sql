set SCHEMA 'public';

CREATE TYPE user_class_enum AS ENUM (
    'EXTERN',
    'PROF',
    'AUTO_1A',
    'AUTO_2A',
    'AUTO_3A',
    'INFO_1A',
    'INFO_2A',
    'INFO_3A',
    'MECA_1A',
    'MECA_2A',
    'MECA_3A',
    'TEXT_1A',
    'TEXT_2A',
    'TEXT_3A',
    'GI_1A',
    'GI_2A',
    'GI_3A',
    'OLD_4A'
    );

CREATE TYPE user_type_enum AS ENUM (
    'USER',
    'ADMIN'
    );

CREATE TYPE game_state_enum AS ENUM (
    'IN_PREPARATION',
    'READY',
    'IN_PROGRESS',
    'FINISHED'
    );

CREATE TABLE users
(
    id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    username   TEXT            NOT NULL,
    lastname   TEXT            NOT NULL,
    user_class user_class_enum NOT NULL,
    type       user_type_enum  NOT NULL,
    UNIQUE (username, lastname)
);

CREATE TABLE question_sets
(
    id   UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name TEXT NOT NULL
);

CREATE TABLE questions
(
    id             UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    text           TEXT NOT NULL,
    set_id         UUID NOT NULL,
    question_order INT  NOT NULL,
    FOREIGN KEY (set_id) REFERENCES question_sets (id)
);

CREATE TABLE answers
(
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    text        TEXT    NOT NULL,
    is_correct  BOOLEAN NOT NULL,
    question_id UUID    NOT NULL,
    FOREIGN KEY (question_id) REFERENCES questions (id)
);

CREATE TABLE games
(
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name                TEXT            NOT NULL,
    state               game_state_enum NOT NULL,
    diffusion           BOOLEAN         NOT NULL,
    set_id              UUID,
    current_question_id UUID,
    FOREIGN KEY (set_id) REFERENCES question_sets (id),
    FOREIGN KEY (current_question_id) REFERENCES questions (id)
);

CREATE TABLE games_users
(
    game_id UUID NOT NULL,
    user_id UUID NOT NULL,
    PRIMARY KEY (game_id, user_id),
    FOREIGN KEY (game_id) REFERENCES games (id),
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE waiting_games_users
(
    game_id UUID NOT NULL,
    user_id UUID NOT NULL,
    PRIMARY KEY (game_id, user_id),
    FOREIGN KEY (game_id) REFERENCES games (id),
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE game_question_results
(
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    game_id     UUID NOT NULL,
    question_id UUID NOT NULL,
    FOREIGN KEY (game_id) REFERENCES games (id),
    FOREIGN KEY (question_id) REFERENCES questions (id)
);

CREATE TABLE game_player_results
(
    id                      UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id                 UUID NOT NULL,
    answer_id               UUID NOT NULL,
    game_question_result_id UUID NOT NULL,
    duration                INT  NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (answer_id) REFERENCES answers (id),
    FOREIGN KEY (game_question_result_id) REFERENCES game_question_results (id)
);
