package com.example.lacdpapel.Models;

import com.google.gson.annotations.SerializedName;

public class Poster {
        @SerializedName("url")
        private String url;

        @SerializedName("previewUrl")
        private String previewUrl;

        public String getUrl() {
            return url;
        }

        public String getPreviewUrl() {
            return previewUrl;
        }


}
