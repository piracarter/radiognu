package org.radiognu.radiognu.model;

public class track {

	private boolean vivo;
	private String title, artist, album, country, year, license;
	private String strImageBase64;
	
	public boolean isVivo() {
		return vivo;
	}
	public void setVivo(boolean vivo) {
		this.vivo = vivo;
	}
	public String getTitle() {
		return title;
	}
	public String getStrImageBase64() {
		return strImageBase64;
	}
	public void setStrImageBase64(String strImageBase64) {
		this.strImageBase64 = strImageBase64;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getArtist() {
		return artist;
	}
	public void setArtist(String artist) {
		this.artist = artist;
	}
	public String getAlbum() {
		return album;
	}
	public void setAlbum(String album) {
		this.album = album;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getLicense() {
		return license;
	}
	public void setLicense(String license) {
		this.license = license;
	}
	
}
