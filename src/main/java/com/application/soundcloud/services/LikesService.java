package com.application.soundcloud.services;

import com.application.soundcloud.repositories.LikesRepository;
import com.application.soundcloud.repositories.TracksRepository;
import com.application.soundcloud.security.jwt.JwtUtils;
import com.application.soundcloud.tables.Likes;
import com.application.soundcloud.tables.Tracks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@Service
public class LikesService {
    @Autowired
    private LikesRepository likesRepository;
    @Autowired
    private TracksRepository tracksRepository;
    @Autowired
    private UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    public String getLikeStatus(String songKey, String UUID){
        if (userService.checkUUID(UUID) && tracksRepository.existsBySongKey(songKey)){
            Optional<Likes> userLike = likesRepository.findBySongKeyAndUUID(songKey, UUID);
            return userLike.isPresent() ? "like" : "unlike";
        }else {
            return "incorrect UUID or SongKey";
        }
    }

    public String updateLikeStatus(String songKey, String UUID){
        String likeStatus = getLikeStatus(songKey, UUID);
        if (likeStatus.equals("incorrect UUID or SongKey")){return likeStatus;}

        if (likeStatus.equals("like")){
            Tracks track = tracksRepository.findBySongKey(songKey);

            track.setLikes(track.getLikes() - 1);
            tracksRepository.save(track);
            Optional<Likes> like = likesRepository.findBySongKeyAndUUID(songKey, UUID);
            likesRepository.delete(like.get());
            return "deleted";

        }else {
            Tracks track = tracksRepository.findBySongKey(songKey);

            track.setLikes(track.getLikes() + 1);
            tracksRepository.save(track);

            Likes newLike = new Likes();

            newLike.setUUID(UUID);
            newLike.setSongKey(songKey);

            likesRepository.save(newLike);
            return "saved";
        }
    }
}
