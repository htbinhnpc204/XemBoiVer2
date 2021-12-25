package com.htbinh.xemboiver2.ui.home.result;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.htbinh.xemboiver2.data.model.Result;
import com.htbinh.xemboiver2.databinding.FragmentResultBinding;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;

public class ResultFragment extends Fragment {

    private FragmentResultBinding binding;


    TextView rsNameMale, rsConGiapMale, rsDoBMale, rsBanMenhMale;
    TextView rsNameFemale, rsConGiapFemale, rsDoBFemale, rsBanMenhFemale;
    TextView nguHanhBody, cungMenhBody, canChiBody, conSoBody, hoTenBody, tongDiem, ketLuan;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentResultBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        rsNameMale = binding.rsNameMale;
        rsConGiapMale = binding.rsConGiapMale;
        rsDoBMale = binding.rsDoBMale;
        rsBanMenhMale = binding.rsBanMenhMale;
        rsNameFemale = binding.rsNameFemale;
        rsConGiapFemale = binding.rsConGiapFemale;
        rsDoBFemale = binding.rsDoBFemale;
        rsBanMenhFemale = binding.rsBanMenhFemale;
        nguHanhBody = binding.nguHanhBody;
        cungMenhBody = binding.cungMenhBody;
        canChiBody = binding.canChiBody;
        conSoBody = binding.conSoBody;
        hoTenBody = binding.hoTenBody;
        tongDiem = binding.tongDiem;
        ketLuan = binding.ketLuan;

        assert getArguments() != null;
        String html = getArguments().getString("html");

        if (!html.equals("")) {
            Document raw = Jsoup.parse(html);
            ArrayList<String> Nam = new ArrayList<>();
            for (Element item :
                    raw.select("div.nam").first().select("li")) {
                Nam.add(item.select("span.red").get(0).text());
            }
            Result mNam = new Result(Nam.get(0), Nam.get(1), Nam.get(2).replaceAll("\\(.*?\\)", ""), Nam.get(3));
            ganDuLieuNam(mNam);
            ArrayList<String> Nu = new ArrayList<>();
            for (Element item :
                    raw.select("div.nu").first().select("li")) {
                Nu.add(item.select("span.red").get(0).text());
            }
            Result mNu = new Result(Nu.get(0), Nu.get(1), Nu.get(2).replaceAll("\\(.*?\\)", ""), Nu.get(3));
            ganDuLieuNu(mNu);
            ArrayList<String> Content = new ArrayList<>();
            for (Element item :
                    raw.select("div.box_luangiai")) {
                if(item.select("table").first() != null){
                    item.select("table").remove();
                }
                Content.add(item.select("div.content").toString());
            }
            Content.add(raw.select("div.ket_luan").select("h3").text());
            raw.select("div.ket_luan").select("h3").remove();
            Content.add(raw.select("div.ket_luan").text());

            ganDuLieuContent(Content);
        }
        // Inflate the layout for this fragment
        return root;
    }

    private void ganDuLieuNam(Result data) {
        rsNameMale.setText(data.getpName());
        rsConGiapMale.setText(data.getpAge());
        rsDoBMale.setText(data.getpDoB());
        rsBanMenhMale.setText(data.getBanMenh());
    }

    private void ganDuLieuNu(Result data) {
        rsNameFemale.setText(data.getpName());
        rsConGiapFemale.setText(data.getpAge());
        rsDoBFemale.setText(data.getpDoB());
        rsBanMenhFemale.setText(data.getBanMenh());
    }

    private void ganDuLieuContent(ArrayList<String> data) {
        nguHanhBody.setText(Html.fromHtml(data.get(0)));
        cungMenhBody.setText(Html.fromHtml(data.get(1)));
        canChiBody.setText(Html.fromHtml(data.get(2)));
        conSoBody.setText(Html.fromHtml(data.get(3)));
        hoTenBody.setText(Html.fromHtml(data.get(4)));
        tongDiem.setText(Html.fromHtml(data.get(5)));
        ketLuan.setText(Html.fromHtml(data.get(6)));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}