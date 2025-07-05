package itu.fromage;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController {
    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    @RequestMapping("/")
    String hello(Model model) {
        model.addAttribute("title", "Gestion Fromagerie - Menu Principal");
        return "hello";
    }

    @RequestMapping("/api/calc")
    @ResponseBody
    Result calc(@RequestParam int left, @RequestParam int right) {
        MapSqlParameterSource source = new MapSqlParameterSource()
                .addValue("left", left)
                .addValue("right", right);
        return jdbcTemplate.queryForObject("SELECT :left + :right AS answer", source,
                (rs, rowNum) -> new Result(left, right, rs.getLong("answer")));
    }

    @Data
    static class Result {
        private final int left;
        private final int right;
        private final long answer;
    }
} 