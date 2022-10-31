package it.polito.tdp.itunes.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.itunes.model.Adiacenza;
import it.polito.tdp.itunes.model.Album;
import it.polito.tdp.itunes.model.Artist;
import it.polito.tdp.itunes.model.Genre;
import it.polito.tdp.itunes.model.MediaType;
import it.polito.tdp.itunes.model.Playlist;
import it.polito.tdp.itunes.model.Track;

public class ItunesDAO {
	
	public List<Album> getAllAlbums(int n){
		
		String sql = "SELECT a.AlbumId, a.Title, SUM(t.Milliseconds) AS durata "
				+ "FROM album a, track t "
				+ "WHERE a.AlbumId = t.AlbumId "
				+ "GROUP BY a.AlbumId "
				+ "HAVING SUM(t.Milliseconds) > ?";
		
		List<Album> result = new LinkedList<>();
		
		int secondi = n*1000;
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, secondi);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Album(res.getInt("AlbumId"), res.getString("Title"), res.getInt("durata")));
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
		return result;
	}
	
public void getAllAlbums(int n, Map<Integer, Album> idMap){
		
		String sql = "SELECT a.AlbumId, a.Title, SUM(t.Milliseconds) AS durata "
				+ "FROM album a, track t "
				+ "WHERE a.AlbumId = t.AlbumId "
				+ "GROUP BY a.AlbumId "
				+ "HAVING SUM(t.Milliseconds) > ?";
		
		int secondi = n*1000;
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, secondi);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				
				if(!idMap.containsKey(res.getInt("AlbumId"))) {
					idMap.put(res.getInt("AlbumId"), new Album(res.getInt("AlbumId"), res.getString("Title"), res.getInt("durata")));
				}
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
	}
	
	public List<Artist> getAllArtists(){
		final String sql = "SELECT * FROM Artist";
		List<Artist> result = new LinkedList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Artist(res.getInt("ArtistId"), res.getString("Name")));
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
		return result;
	}
	
	public List<Playlist> getAllPlaylists(){
		final String sql = "SELECT * FROM Playlist";
		List<Playlist> result = new LinkedList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Playlist(res.getInt("PlaylistId"), res.getString("Name")));
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
		return result;
	}
	
	public List<Track> getAllTracks(){
		final String sql = "SELECT * FROM Track";
		List<Track> result = new ArrayList<Track>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Track(res.getInt("TrackId"), res.getString("Name"), 
						res.getString("Composer"), res.getInt("Milliseconds"), 
						res.getInt("Bytes"),res.getDouble("UnitPrice")));
			
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
		return result;
	}
	
	public List<Genre> getAllGenres(){
		final String sql = "SELECT * FROM Genre";
		List<Genre> result = new LinkedList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Genre(res.getInt("GenreId"), res.getString("Name")));
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
		return result;
	}
	
	public List<MediaType> getAllMediaTypes(){
		final String sql = "SELECT * FROM MediaType";
		List<MediaType> result = new LinkedList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new MediaType(res.getInt("MediaTypeId"), res.getString("Name")));
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
		return result;
	}
	
	public List<Adiacenza> getAdiacenze(int n, Map<Integer, Album> idMap){
		
		String sql = "SELECT a1.AlbumId, a2.AlbumId "
				+ "FROM album a1, track t1, album a2 "
				+ "WHERE a1.AlbumId = t1.AlbumId AND a1.AlbumId > a2.AlbumId "
				+ "AND a2.AlbumId IN "
				+ "(SELECT a.AlbumId "
				+ "FROM album a, track t "
				+ "WHERE a.AlbumId = t.AlbumId "
				+ "GROUP BY a.AlbumId "
				+ "HAVING SUM(t.Milliseconds) > ?) "
				+ "GROUP BY a1.AlbumId, a2.AlbumId "
				+ "HAVING SUM(t1.Milliseconds) > ?";
		
		int secondi = n*1000;
		List<Adiacenza> result = new LinkedList<Adiacenza>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, secondi);
			st.setInt(2, secondi);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				
				Album a1 = idMap.get(res.getInt("a1.AlbumId"));
				Album a2 = idMap.get(res.getInt("a2.AlbumId"));
				int peso = a1.getDurata()+a2.getDurata();
				
				if(peso > 4*secondi) {
					if(a1.getDurata() < a2.getDurata()) {
						result.add(new Adiacenza(a1, a2, peso));
					}
					else if(a2.getDurata() > a1.getDurata()) {
						result.add(new Adiacenza(a2, a1, peso));
					}
				}
			}
			conn.close();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
	}
	
}
