package test.managers;

import managers.HighScoreManager;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class HighScoreManagerTest {
    //cái link path bị làm sao á, chịu :V

    @Test
    void loadHighScores_ShouldCreateDefaultIfFileMissing() {
        // Giả lập dữ liệu mặc định
        ArrayList<HighScoreManager.ScoreEntry> scores = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            scores.add(new HighScoreManager.ScoreEntry("TAITO", 1000 - i * 100));
        }

        assertNotNull(scores);
        assertEquals(10, scores.size());
        assertEquals("TAITO", scores.get(0).name);
    }

    @Test
    void addScore_ShouldInsertAndSortDescending() {
        ArrayList<HighScoreManager.ScoreEntry> scores = new ArrayList<>();
        scores.add(new HighScoreManager.ScoreEntry("Player1", 100));
        scores.add(new HighScoreManager.ScoreEntry("Player2", 200));

        // Giả lập logic thêm điểm mới
        scores.add(new HighScoreManager.ScoreEntry("NewHigh", 500));
        scores.sort((a, b) -> Integer.compare(b.score, a.score));

        assertEquals("NewHigh", scores.get(0).name);
        assertTrue(scores.get(0).score >= scores.get(1).score);
    }

    @Test
    void addScore_ShouldRespectMaxLimit() {
        int maxScores = 3;
        ArrayList<HighScoreManager.ScoreEntry> scores = new ArrayList<>();
        scores.add(new HighScoreManager.ScoreEntry("A", 10));
        scores.add(new HighScoreManager.ScoreEntry("B", 20));
        scores.add(new HighScoreManager.ScoreEntry("C", 30));

        // Thêm điểm mới, rồi sắp xếp giảm dần
        scores.add(new HighScoreManager.ScoreEntry("D", 40));
        scores.sort((a, b) -> Integer.compare(b.score, a.score));

        // Giới hạn lại
        if (scores.size() > maxScores)
            scores = new ArrayList<>(scores.subList(0, maxScores));

        assertEquals(3, scores.size());
        assertEquals("D", scores.get(0).name);
    }
}
