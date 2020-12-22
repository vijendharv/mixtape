package com.vijay;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vijay.model.Playlist;
import com.vijay.model.Song;
import com.vijay.model.User;
import com.vijay.model.change.AddPlaylist;
import com.vijay.model.change.AddSongToPlaylist;
import com.vijay.model.change.Change;
import com.vijay.model.Mixtape;
import com.vijay.model.change.RemovePlaylist;
import org.apache.log4j.Logger;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MixtapeController {

    private final Logger logger = Logger.getLogger(MixtapeController.class);

    private final Mixtape mixtape;
    private final List<Change> changes;
    private final Set<Integer> songIds;
    private final Set<Integer> playlistIds;
    private final Set<Integer> userIds;
    private int currPlaylistId = Integer.MIN_VALUE;

    public MixtapeController(Mixtape mixtape, List<Change> changes){
        this.mixtape = mixtape;
        this.changes = changes;
        songIds = new HashSet<>();
        playlistIds = new HashSet<>();
        userIds = new HashSet<>();
        init();
    }

    /**
     * Initializes the Sets that were created as part of the class initializer
     */
    private void init(){
        for(Song song: mixtape.getSongs()){
            songIds.add(song.getId());
        }

        for(Playlist playlist: mixtape.getPlaylists()){
            playlistIds.add(playlist.getId());
            currPlaylistId = Math.max(currPlaylistId, playlist.getId());
        }

        for(User user: mixtape.getUsers()){
            userIds.add(user.getId());
        }
    }

    /**
     * Applies changes to the mixtape
     */
    public void mix(){
        for(Change change: changes){
            if (change instanceof AddPlaylist){
                addPlaylist((AddPlaylist) change);
            } else if (change instanceof RemovePlaylist){
                removePlaylist((RemovePlaylist)change);
            } else if (change instanceof AddSongToPlaylist){
                addSongToPlaylist((AddSongToPlaylist)change);
            }
        }
    }

    /**
     * Adds a playlist to mix tape
     * @param addPlaylist - The playlist that needs to be added.
     */
    private void addPlaylist(AddPlaylist addPlaylist){
        boolean isValid = true;
        if (!userIds.contains(addPlaylist.getUserId())){
            logger.error("AddPlayList: The user with user_id: " + addPlaylist.getUserId() + " is not a valid user.");
            isValid = false;
        }

        for(int songId: addPlaylist.getSongIds()){
            if (!songIds.contains(songId)) {
                logger.error("AddPlaylist: The song with song_id: " + songId + " is not a valid song.");
                isValid = false;
            }
        }

        if (!isValid){
            log(addPlaylist, Constants.LOG_TYPE_ERROR);
            return;
        }

        Playlist playlist = new Playlist();
        playlist.setId(++currPlaylistId);
        playlist.setSongIds(addPlaylist.getSongIds());
        playlist.setUserId(addPlaylist.getUserId());

        playlistIds.add(currPlaylistId);
        mixtape.getPlaylists().add(playlist);
    }

    /**
     * Removes a playlist from mix tape.
     * @param removePlaylist - The playlist that needs to be removed.
     */
    private void removePlaylist(RemovePlaylist removePlaylist){
        if (!playlistIds.contains(removePlaylist.getPlaylistId())) {
            logger.error("RemovePlayList: The playlist with playlist_id: " + removePlaylist.getPlaylistId() + " is not a valid playlist.");
            log(removePlaylist, Constants.LOG_TYPE_ERROR);
            return;
        }

        for(int i=0; i<mixtape.getPlaylists().size(); i++){
            if (mixtape.getPlaylists().get(i).getId() == removePlaylist.getPlaylistId()) {
                mixtape.getPlaylists().remove(i);
                break;
            }
        }

        playlistIds.remove(removePlaylist.getPlaylistId());
    }

    /**
     * Add song to a play list
     * @param addSongToPlayList - The song that should be added to playlist
     */
    private void addSongToPlaylist(AddSongToPlaylist addSongToPlayList){
        boolean isValid = true;
        if(!songIds.contains(addSongToPlayList.getSongID())) {
            logger.error("AddSongToPlaylist: The song with song_id: " + addSongToPlayList.getSongID() + " is not a valid song.");
            isValid = false;
        }

        if (!playlistIds.contains(addSongToPlayList.getPlaylistID())) {
            logger.error("AddSongToPlaylist: The play list with playlist_id: " + addSongToPlayList.getPlaylistID() + " is not a valid playlist.");
            isValid = false;
        }

        if (!isValid){
            log(addSongToPlayList, Constants.LOG_TYPE_ERROR);
            return;
        }

        for(Playlist playlist: mixtape.getPlaylists()){
            if (playlist.getId() == addSongToPlayList.getPlaylistID()) {
                // Check if the song already exists. If exists then do not add else add the song.
                for(int songId: playlist.getSongIds()){
                    if (songId == addSongToPlayList.getSongID()) {
                        logger.info("The song with song_id: " + songId + " already exists in playlist with playlist_id: " + playlist.getId());
                        log(addSongToPlayList, Constants.LOG_TYPE_INFO);
                        return;
                    }
                }
                playlist.getSongIds().add(addSongToPlayList.getSongID());
            }
        }
    }

    /**
     * Prints the serialized object in the log file.
     * @param obj - Object that needs to be serialized
     * @param type - Type of log
     */
    private void log(Object obj, String type){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            if (type.equalsIgnoreCase(Constants.LOG_TYPE_ERROR))
                logger.error(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj));
            else if (type.equalsIgnoreCase(Constants.LOG_TYPE_INFO))
                logger.info(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj));
        }
        catch(Exception e){
            // We should never get here....
            logger.error("Encountered an error while serializing object.", e);
        }
    }

    public Mixtape getMixtape() {
        return mixtape;
    }
}
