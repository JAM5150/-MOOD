package com.example.backend.music;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.model.BasicResponse;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin
@AllArgsConstructor
@ApiResponses(value = { @ApiResponse(code = 401, message = "Unauthorized", response = BasicResponse.class),
        @ApiResponse(code = 403, message = "Forbidden", response = BasicResponse.class),
        @ApiResponse(code = 404, message = "Not Found", response = BasicResponse.class),
        @ApiResponse(code = 500, message = "Failure", response = BasicResponse.class) })
@RequestMapping("/music")
@RestController
public class MusicController {

    private MusicService musicService;


    @GetMapping("/update")
    @ApiOperation(value = "음악 최신화작업")
    public String UpdateMusic() throws IOException, ParseException {
        musicService.updateMusic();
        return "Success";
    }

    @GetMapping("/{id}/{diary_id}/{mid}")
    public ResponseEntity<Map<String, Object>> enrollMusic(
            @PathVariable int diary_id,
            @PathVariable int mid,
            @PathVariable String id) {
        HashMap<String, Object> resultMap = new HashMap<>();
        musicService.enrollMusic(id, diary_id, mid);
        return new ResponseEntity<>(resultMap, HttpStatus.OK);

    }
}