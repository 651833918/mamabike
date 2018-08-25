package cn.powerr.mamabike;

import cn.powerr.mamabike.bike.entity.BikeLocation;
import cn.powerr.mamabike.bike.entity.Point;
import cn.powerr.mamabike.bike.service.BikeGeoService;
import cn.powerr.mamabike.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@Slf4j
@SpringBootTest(classes = MamabikeApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MamabikeApplicationTests {

    @Autowired
    private TestRestTemplate testRestTemplate;
    @LocalServerPort
    private int port;
    @Autowired
    private UserService userService;
    @Autowired
    private BikeGeoService bikeGeoService;

    @Test
    public void contextLoads() {
        String result = testRestTemplate.getForObject("/user/hello", String.class);
        System.out.println(result);
    }

    @Test
    public void geoTest(){
        List<BikeLocation> bikeLocations = bikeGeoService.nearBikes("bike_location", "location", new Point(104.063339, 30.547347),
                0, 500, null, null, 10);
        System.out.println("done!");
    }



}
