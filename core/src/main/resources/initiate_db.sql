CREATE TABLE IF NOT EXISTS UserAnswers (
    id INTEGER PRIMARY KEY,
    ticket INT NOT NULL,
    question INT NOT NULL,
    chosen_answer INT NOT NULL,
    is_right VARCHAR(6)
)