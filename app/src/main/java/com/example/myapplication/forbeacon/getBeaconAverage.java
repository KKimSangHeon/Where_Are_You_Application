package com.example.myapplication.forbeacon;

import com.example.myapplication.socketdata.AverageRSSI;
import com.perples.recosdk.RECOBeacon;

import java.util.ArrayList;

/**
 * Created by SangHeon on 2016-05-14.
 */
/*
*  Created by KSH on 2016-03-19.
*  비콘 데이터의 평균치를 구하기 위한 클래스
*  사용법
*  이 클래스의 인스턴스 생성 후 mGetBeaconAverage 메소드를 호출한다
*  비콘을 인식할 때 마다 데이터를 축적하므로 읽을 횟수만큼 설정 후 브레이크(unbind)를 걸어준다.
*/
public class getBeaconAverage{ //

    BeaconDupchecker BD=new BeaconDupchecker();
    int dupchecker;

    public ArrayList<AverageRSSI> mGetBeaconAverage(ArrayList<RECOBeacon> rb, ArrayList<AverageRSSI> arrayRSSI)
    {
        for(int i=0;i<rb.size();i++)
        {
            dupchecker=BD.checkDuplication(arrayRSSI, rb.get(i).getMinor());

            if(dupchecker==99999)   //존재하지 않는 Minor일 경우
            {
                AverageRSSI temp=new AverageRSSI(rb.get(i).getMinor(),rb.get(i).getRssi(),rb.get(i).getAccuracy());
                arrayRSSI.add(temp);
            }
            else
                arrayRSSI.get(dupchecker).increase(rb.get(i).getRssi(),rb.get(i).getAccuracy());

        }

        return arrayRSSI;
    }




}


/*
*  Created by KSH on 2016-03-19.
*  비콘 데이터의 중복 확인을 위한 클래스
*  getBeaconAverage 클래스를 위해 정의됨
*/
class BeaconDupchecker {
    public int checkDuplication(ArrayList<AverageRSSI> AR, int minor) {
        for (int i = 0; i < AR.size(); i++) {
            if (AR.get(i).minor == minor)  //중복이 되면 인덱스 반환
                return i;
        }
        return 99999;   //중복되지 않을경우 99999반환
    }
}
