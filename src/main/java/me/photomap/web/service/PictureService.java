package me.photomap.web.service;

import com.amazonaws.util.JodaTime;
import me.photomap.web.data.repo.PicturesRepo;
import me.photomap.web.data.repo.model.Picture;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.ISODateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.*;


@Service
public class PictureService {


  @Autowired private PicturesRepo picturesRepo;


  public List<Picture> findPicturesFromAndTo(long from, long to, String user){
    //for each year from find picture for the range of months
    DateTime fdt = new DateTime(from * 1000);
    DateTime tdt = new DateTime(to * 1000);
    int fromYear = fdt.getYear();
    int toYear = tdt.getYear();
    int fromMonth = fdt.getMonthOfYear();
    int toMonth = tdt.getMonthOfYear();

    int numYears = toYear - fromYear;
    List<Picture> pics;
    Calendar c = new GregorianCalendar(fromYear,toMonth,1);
    int days = c.getActualMaximum(Calendar.DAY_OF_MONTH);
    c = new GregorianCalendar(fromYear,toMonth,days);//to
    Calendar fc = new GregorianCalendar(fromYear,fromMonth,1);//from
    pics = picturesRepo.finAllInDateRange((fc.getTimeInMillis() / 1000),(c.getTimeInMillis() / 1000),user);
    for(int i=1; i <= numYears; i++){
      int fYear = fromYear + i;
      c = new GregorianCalendar(fYear,toMonth,days);
      fc = new GregorianCalendar(fYear,fromMonth,1);
      pics.addAll(picturesRepo.finAllInDateRange((fc.getTimeInMillis() / 1000),(c.getTimeInMillis() / 1000),user));
    }


    return  pics;
  }

}
