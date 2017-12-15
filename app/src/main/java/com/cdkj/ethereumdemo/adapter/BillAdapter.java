package com.cdkj.ethereumdemo.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.cdkj.baselibrary.utils.DateUtil;
import com.cdkj.ethereumdemo.R;
import com.cdkj.ethereumdemo.model.BillModel;
import com.cdkj.ethereumdemo.util.AccountUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.math.BigDecimal;
import java.util.List;

import static com.cdkj.baselibrary.utils.DateUtil.DATE_DAY;
import static com.cdkj.baselibrary.utils.DateUtil.DATE_HM;
import static com.cdkj.baselibrary.utils.DateUtil.DATE_M;
import static com.cdkj.baselibrary.utils.DateUtil.DATE_YM;

/**
 * Created by lei on 2017/8/22.
 */

public class BillAdapter extends BaseQuickAdapter<BillModel.ListBean,BaseViewHolder> {

    List<BillModel.ListBean> list;

    public BillAdapter(@Nullable List<BillModel.ListBean> data) {
        super (R.layout.item_bill,data);
    }

    @NonNull
    @Override
    public List<BillModel.ListBean> getData() {
        return super.getData();
    }

    @Override
    protected void convert(BaseViewHolder helper, BillModel.ListBean item) {
        if (list == null){
            list = getData();
        }

        if (helper.getLayoutPosition() == 0){
            helper.setVisible(R.id.tv_ym, true);
            helper.setText(R.id.tv_ym, DateUtil.formatStringData(item.getCreateDatetime(),DATE_YM));
        }else {
            String month_now = DateUtil.formatStringData(item.getCreateDatetime(),DATE_M);
            String month_last = DateUtil.formatStringData(list.get(helper.getLayoutPosition()-1).getCreateDatetime(),DATE_M);

            if (!month_now.equals(month_last)){
                helper.setVisible(R.id.tv_ym, true);
                helper.setText(R.id.tv_ym, DateUtil.formatStringData(item.getCreateDatetime(),DATE_YM));
            }else {
                helper.setVisible(R.id.tv_ym, false);
            }
        }

        helper.setText(R.id.tv_day, DateUtil.formatStringData(item.getCreateDatetime(),DATE_DAY));
        helper.setText(R.id.tv_time, DateUtil.formatStringData(item.getCreateDatetime(),DATE_HM));

        helper.setText(R.id.tv_remark, item.getBizNote());
        helper.setText(R.id.tv_currency, item.getCurrency());

        BigDecimal tas = new BigDecimal(item.getTransAmountString());
        int i=tas.compareTo(BigDecimal.ZERO);
        if (i==1){
            helper.setText(R.id.tv_amount, "+" + AccountUtil.weiToEth(tas));
        }else {
            helper.setText(R.id.tv_amount, AccountUtil.weiToEth(tas));
        }

        if (item.getKind().equals("0")){ // 非冻结流水
            switch (item.getBizType()){
                case "charge": // 充值
                    helper.setImageResource(R.id.iv_type, R.mipmap.bill_charge);
                    break;

                case "withdraw": // 取现
                    helper.setImageResource(R.id.iv_type, R.mipmap.bill_withdraw);
                    break;

                case "buy": // 买入
                    helper.setImageResource(R.id.iv_type, R.mipmap.bill_into);
                    break;

                case "sell": // 卖出
                    helper.setImageResource(R.id.iv_type, R.mipmap.bill_out);
                    break;

                case "tradefee": // 手续费
                case "withdrawfee": // 手续费
                    helper.setImageResource(R.id.iv_type, R.mipmap.bill_fee);
                    break;


                default:
                    helper.setImageResource(R.id.iv_type, R.mipmap.bill_award);
                    break;

            }
        }else { // 冻结流水
            if (item.getTransAmountString().contains("-")){ // 金额是负数
                helper.setImageResource(R.id.iv_type, R.mipmap.bill_withdraw);
            }else {
                helper.setImageResource(R.id.iv_type, R.mipmap.bill_charge);
            }
        }



    }
}
