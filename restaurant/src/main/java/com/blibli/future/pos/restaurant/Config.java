package com.blibli.future.pos.restaurant;

class Config {
    private Jdbc jdbc;

    public Config() {
    }

    public Jdbc getJdbc() {
        return jdbc;
    }

    public void setJdbc(Jdbc jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public String toString() {
        return "Config{" +
                "jdbc=" + jdbc +
                '}';
    }

    class Jdbc {
        private String url;
        private String username;
        private String password;

        public Jdbc() {
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        @Override
        public String toString() {
            return "Jdbc{" +
                    "url='" + url + '\'' +
                    ", username='" + username + '\'' +
                    ", password='" + password + '\'' +
                    '}';
        }
    }
}
