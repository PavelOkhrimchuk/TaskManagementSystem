-- User
CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       name VARCHAR(255) NOT NULL,
                       role VARCHAR(50) CHECK (role IN ('ADMIN', 'USER')) NOT NULL,
                       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Task
CREATE TABLE tasks (
                       id BIGSERIAL PRIMARY KEY,
                       title VARCHAR(255) NOT NULL,
                       description VARCHAR(1000) NOT NULL,
                       status VARCHAR(50) CHECK (status IN ('PENDING', 'IN_PROGRESS', 'COMPLETED')) NOT NULL,
                       priority VARCHAR(50) CHECK (priority IN ('HIGH', 'MEDIUM', 'LOW')) NOT NULL,
                       author_id BIGINT NOT NULL,
                       executor_id BIGINT,
                       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       CONSTRAINT fk_tasks_author FOREIGN KEY (author_id) REFERENCES users (id) ON DELETE CASCADE,
                       CONSTRAINT fk_tasks_executor FOREIGN KEY (executor_id) REFERENCES users (id) ON DELETE SET NULL
);

-- Comment
CREATE TABLE comments (
                          id BIGSERIAL PRIMARY KEY,
                          content VARCHAR(1000) NOT NULL,
                          author_id BIGINT NOT NULL,
                          task_id BIGINT NOT NULL,
                          created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          CONSTRAINT fk_comments_author FOREIGN KEY (author_id) REFERENCES users (id) ON DELETE CASCADE,
                          CONSTRAINT fk_comments_task FOREIGN KEY (task_id) REFERENCES tasks (id) ON DELETE CASCADE
);
