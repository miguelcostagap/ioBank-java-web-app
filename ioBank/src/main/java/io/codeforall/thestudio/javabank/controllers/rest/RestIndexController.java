package io.codeforall.thestudio.javabank.controllers.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class RestIndexController {

    @RequestMapping(method = RequestMethod.GET, path = {"/", ""})
    protected ApiVersion showVersion() {

        ApiVersion version = new ApiVersion();
        version.setName("JavaBank API");
        version.setVersion("v0.1");

        return version;

    }

    private static class ApiVersion {

        private String name;
        private String version;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }
    }
}
