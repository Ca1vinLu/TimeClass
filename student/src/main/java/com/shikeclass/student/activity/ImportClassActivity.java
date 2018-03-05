package com.shikeclass.student.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.shikeclass.student.R;
import com.shikeclass.student.base.BaseActivity;
import com.shikeclass.student.bean.ClassBean;
import com.shikeclass.student.eventbus.UpdateTableEvent;
import com.shikeclass.student.utils.CommonValue;
import com.shikeclass.student.utils.DialogUtils;
import com.shikeclass.student.utils.SharedPreUtil;
import com.shikeclass.student.utils.UrlUtils;

import org.angmarch.views.NiceSpinner;
import org.greenrobot.eventbus.EventBus;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

public class ImportClassActivity extends BaseActivity {


    @BindView(R.id.btn_import)
    TextView btnImport;
    @BindView(R.id.spinner_academic_year)
    NiceSpinner spinnerAcademicYear;
    @BindView(R.id.spinner_term)
    NiceSpinner spinnerTerm;
    @BindView(R.id.spinner_faculty)
    NiceSpinner spinnerFaculty;
    @BindView(R.id.spinner_grade)
    NiceSpinner spinnerGrade;
    @BindView(R.id.spinner_school_class)
    NiceSpinner spinnerSchoolClass;


    private String[] academicYear = {"2017-2018",
            "2016-2017",
            "2015-2016",
            "2014-2015",
            "2013-2014",
            "2012-2013"};

    private String[] term = {"一", "二"};

    private String[][] faculty = {{"11", "机电及自动化学院"},
            {"12", "土木工程学院"},
            {"13", "建筑学院"},
            {"14", "材料科学与工程学院"},
            {"15", "信息科学与工程学院"},
            {"16", "工商管理学院"},
            {"17", "法学院"},
            {"18", "文学院"},
            {"19", "外国语学院"},
            {"20", "美术学院"},
            {"21", "数学科学学院"},
            {"22", "公共管理学院"},
            {"23", "旅游学院"},
            {"24", "经济与金融学院"},
            {"25", "计算机科学与技术学院"},
            {"26", "化工学院"},
            {"27", "哲学与社会发展学院"},
            {"30", "体育学院"},
            {"33", "境外生"},
            {"95", "工学院"},
            {"97", "音乐舞蹈学院"},
            {"98", "华文学院"},
            {"34", "生物医学学院"},
            {"28", "马克思主义学院"},
            {"29", "国际学院"},
            {"32", "其他"},
            {"93", "厦航学院"},
            {"35", "国际关系学院"},
            {"36", "统计学院"},
            {"37", "新闻与传播学院"},
            {"51", "创新创业学院"}};

    private String[] grade = {"2017",
            "2016",
            "2015",
            "2014",
            "2013",
            "2012",
            "2011",
            "2010"};

    private String[][] schoolClass;


    private List<ClassBean> classList = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_import_class;
    }

    @Override
    public void initView() {
//        DialogUtils.showDialog(mActivity, "提示", "请确保手机已连接至HQU", "好的", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//            }
//        });
    }

    @Override
    public void initData() {
        getSchoolClass();
        List<String> facultyName = new ArrayList<>();
        for (String[] strings : faculty) {
            facultyName.add(strings[1]);
        }

        spinnerAcademicYear.attachDataSource(Arrays.asList(academicYear));
        spinnerTerm.attachDataSource(Arrays.asList(term));
        spinnerFaculty.attachDataSource(facultyName);
        spinnerGrade.attachDataSource(Arrays.asList(grade));
    }

    @Override
    public void initListener() {
        btnImport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getClassTable();
            }
        });

        spinnerFaculty.addOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getSchoolClass();
            }
        });

        spinnerGrade.addOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getSchoolClass();
            }
        });
    }


    private void getSchoolClass() {
        final ProgressDialog progressDialog = new ProgressDialog(mActivity);
        progressDialog.setMessage("正在获取班级列表");
        progressDialog.show();
        OkGo.<String>get(UrlUtils.SpecialityClass)
                .tag(mActivity)
                .params("XYDM", faculty[spinnerFaculty.getSelectedIndex()][0])
                .params("ZYNJ", grade[spinnerGrade.getSelectedIndex()])
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String resp = response.body();
                        String[] temp;
                        if (!TextUtils.isEmpty(resp)) {
                            temp = resp.split(",");
                            String[] item;
                            List<String> className = new ArrayList<>();
                            schoolClass = new String[temp.length - 1][2];
                            for (int i = 0; i < temp.length - 1; i++) {
                                item = temp[i].split(":");
                                schoolClass[i][0] = item[0];
                                schoolClass[i][1] = item[1];
                                className.add(item[1]);
                            }

                            spinnerSchoolClass.attachDataSource(className);
                        }
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        progressDialog.dismiss();
                        DialogUtils.showDialog(mActivity, "提示", "获取班级列表失败，请确保手机已连接至HQU", "好的", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                getSchoolClass();
                            }
                        });
                    }
                });
    }

    private void getClassTable() {
        final ProgressDialog progressDialog = new ProgressDialog(mActivity);
        progressDialog.setMessage("正在导入课程表");
        progressDialog.show();
        OkGo.<String>get(UrlUtils.ClassArrange)
                .tag(mActivity)
                .params("KKXN", academicYear[spinnerAcademicYear.getSelectedIndex()])
                .params("KKXQ", term[spinnerTerm.getSelectedIndex()])
                .params("ZYBJDM", schoolClass[spinnerSchoolClass.getSelectedIndex()][0])
                .params("Type", "班级课表查询")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        parseHTMLResp(response.body());
                        progressDialog.dismiss();
                        DialogUtils.showDialog(mActivity, "提示", "导入成功", "确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EventBus.getDefault().post(new UpdateTableEvent());
                                finish();
                            }
                        });
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        progressDialog.dismiss();
                        DialogUtils.showDialog(mActivity, "提示", "获取课程表失败，请确保手机已连接至HQU", "好的", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                    }
                });
    }

    private void parseHTMLResp(String resp) {
        Document document = Jsoup.parse(resp);
        Elements elements = document.select("table").select("tr");
        for (Element element : elements) {
            Elements td = element.select("td");
            if (td.size() > 0)
                switch (td.get(0).text()) {
                    case "一":
                        addClass(1, td);
                        break;
                    case "二":
                        addClass(2, td);
                        break;
                    case "三":
                        addClass(3, td);
                        break;
                    case "四":
                        addClass(4, td);
                        break;
                    case "五":
                        addClass(5, td);
                        break;
                    case "六":
                        addClass(6, td);
                        break;
                    case "日":
                        addClass(7, td);
                        break;
                    default:
                        break;
                }
        }

        Gson gson = new Gson();
        SharedPreUtil.putStringValue(mActivity, CommonValue.SHA_CLASS_TABLE, gson.toJson(classList));
    }

    private void addClass(int weekDay, Elements classEle) {
        ClassBean bean;
        for (int i = 0; i < classEle.size(); i++) {
            Elements div = classEle.get(i).select("div");
            if (i != 0 && div.size() > 0) {
                bean = new ClassBean();
                classList.add(bean);
                bean.weekDay = weekDay;
                bean.name = div.get(0).text();
                if (bean.name.startsWith("Δ"))
                    bean.name = bean.name.substring(1);
                switch (i) {
                    case 1:
                        bean.startClass = 1;
                        bean.endClass = 2;
                        break;
                    case 2:
                        bean.startClass = 3;
                        bean.endClass = 4;
                        break;
                    case 3:
                        bean.startClass = 5;
                        bean.endClass = 6;
                        break;
                    case 4:
                        bean.startClass = 7;
                        bean.endClass = 8;
                        break;
                    case 5:
                        bean.startClass = 9;
                        bean.endClass = 10;
                        break;
                    case 6:
                        bean.startClass = 11;
                        bean.endClass = 12;
                        break;
                }
                for (int i1 = 1; i1 < div.size(); i1++) {
                    String s = div.get(i1).text();
                    if (TextUtils.isEmpty(s))
                        continue;
                    else if (s.contains("单"))
                        bean.isSingleOrDouble = 1;
                    else if (s.contains("双"))
                        bean.isSingleOrDouble = 2;
                    else if (s.endsWith("周")) {
                        String temp = s.substring(0, s.length() - 1);
                        String[] weeks = temp.split("-");
                        if (weeks.length == 2) {
                            bean.startWeek = Integer.parseInt(weeks[0]);
                            bean.endWeek = Integer.parseInt(weeks[1]);
                        }
                    } else if (s.contains("3节")) {
                        bean.endClass++;
                        if (s.length() > 4)
                            bean.address = s.substring(0, s.length() - 4);
                    } else if (s.contains("4节")) {
                        bean.endClass += 2;
                        if (s.length() > 4)
                            bean.address = s.substring(0, s.length() - 4);
                    } else if (s.startsWith("(") && s.endsWith(")")) {
                        bean.name += s;
                    } else {
                        bean.address = s;
                    }
                }
            }
        }

    }
}
