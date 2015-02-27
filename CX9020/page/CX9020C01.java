/*------------------------------------------------------------------------------
 * PROGNER EX for Java Servlet
 * Copyright(C) 2008 - 2014 Canon IT Solutions Inc. All rights reserved.
 *----------------------------------------------------------------------------*/
package jp.co.canonits.prognerex.aptemplate_desktopaplike.CX9020.page;

import jp.co.canonits.prognerex.aptemplate_desktopaplike.CX9010.service.CX9010S01;
import jp.co.canonits.prognerex.aptemplate_desktopaplike.dto.LoginModel;
import jp.co.canonits.prognerex.aptemplate_desktopaplike.page.BaseDialog;
import jp.co.canonits.prognerex.aptemplate_desktopaplike.session.AppSession;
import jp.co.canonits.prognerex.core.presentation_wicket.component.ExFieldSet;

/**
 * <p>ダイアログテンプレート</p>
 * 
 * @author Canon IT Solutions Inc. R&amp;D Center
 * @version 2.3
 */
public class CX9020C01 extends BaseDialog{

    /**
     * シリアルバージョンUID
     */
    private static final long serialVersionUID = 1L;

    // --------------------------------------------------------------------------
    // 内部定数
    // --------------------------------------------------------------------------

    // --------------------------------------------------------------------------
    // 内部変数
    // --------------------------------------------------------------------------

    // --------------------------------------------------
    // 条件部
    // --------------------------------------------------

    /**
     * 条件部パネル
     */
    protected ExFieldSet pnlCondition;

    // --------------------------------------------------
    // 明細部
    // --------------------------------------------------

    /**
     * 明細部パネル
     */
    protected ExFieldSet pnlDetail;

    // --------------------------------------------------
    // リスト要素
    // --------------------------------------------------

    // --------------------------------------------------
    // ダイアログ
    // --------------------------------------------------

    // --------------------------------------------------------------------------
    // 公開メソッド
    // --------------------------------------------------------------------------

    /**
     * <p>起動条件 : コンストラクタ</p>
     * <p>処理概要 : 画面レイアウトを生成する</p>
     */
    public CX9020C01(){

        // --------------------------------------------------
        // プログラム情報
        // --------------------------------------------------
        // 処理ID
        setOperationId("CX9020");
        // プログラムID
        setProgramId("CX9020C01");
        // プログラム名
        setProgramName("ダイアログテンプレート");
        // プログラムリビジョン
        setProgramRevision("01.00");

        // --------------------------------------------------
        // アプリケーション生成
        // --------------------------------------------------
        // 画面タイトルを設定
        this.setTitle();
        // 画面コンポーネントを生成
        this.initializeComponents();
        // 初期化処理を実行
        this.initializeEvent();
    }

    /**
     * <p>起動条件 : サービス呼出し時</p>
     * <p>処理概要 : サービスを生成し返却する</p>
     * 
     * @return サービス
     */
    protected CX9010S01 getService(){
        AppSession session = (AppSession)this.getSession();
        LoginModel loginModel = session.getLoginModel();
        CX9010S01 service = new CX9010S01();
        service.setLoginModel(loginModel);
        return service;
    }

    /**
     * <p>起動条件 : 初期処理時</p>
     * <p>処理概要 : 画面コンポーネントを生成する</p>
     */
    protected void initializeComponents(){

        // --------------------------------------------------
        // 条件部の生成
        // --------------------------------------------------
        // 条件部パネル
        this.pnlCondition = new ExFieldSet("pnlCondition", "lblCondition", "条件部");
        this.getForm().add(this.pnlCondition);

        // パネルを使用不可に設定
        this.setContainerEnabled(this.pnlCondition, false);

        // --------------------------------------------------
        // 明細部の生成
        // --------------------------------------------------
        // 明細部パネル
        this.pnlDetail = new ExFieldSet("pnlDetail", "lblDetail", "明細部");
        this.getForm().add(this.pnlDetail);

        // パネルを使用不可に設定
        this.setContainerEnabled(this.pnlDetail, false);

        // --------------------------------------------------
        // ファンクション部のラベル設定
        // --------------------------------------------------

        // --------------------------------------------------
        // ファンクション部のスクリプト設定
        // --------------------------------------------------

        // --------------------------------------------------
        // ファンクション部のAJAXイベント設定
        // --------------------------------------------------

        // --------------------------------------------------
        // ファンクション部のダイアログ設定
        // --------------------------------------------------

    }

    /**
     * <p>先頭項目にフォーカスを設定する</p>
     */
    protected void setFocusToFirstItem(){

        // 条件部
        if(this.pnlCondition.isEnabled()){

            // 明細部
        }else{

        }
    }

    /**
     * <p>起動条件 : 初期処理時</p>
     * <p>処理概要 : 前処理を実行する</p>
     * 
     * @return 初期処理における前処理の成否
     */
    @Override
    protected boolean preInit(){
        return true;
    }

    /**
     * <p>起動条件 : 初期処理時</p>
     * <p>処理概要 : 本処理を実行する</p>
     * 
     * @return 初期処理における本処理の成否
     */
    @Override
    protected boolean executeInit(){
        return true;
    }

    /**
     * <p>起動条件 : 初期処理時</p>
     * <p>処理概要 : 後処理を実行する</p>
     * 
     * @return 初期処理における後処理の成否
     */
    @Override
    protected boolean postInit(){
        return true;
    }

    /**
     * <p>起動条件 : F1(閉じる)押下時</p>
     * <p>処理概要 : 前処理を実行する</p>
     * 
     * @return F1(閉じる)処理における前処理の成否
     */
    @Override
    protected boolean preClose(){
        return true;
    }

    /**
     * <p>起動条件 : F1(閉じる)押下時</p>
     * <p>処理概要 : 本処理を実行する</p>
     * 
     * @return F1(閉じる)処理における本処理の成否
     */
    @Override
    protected boolean executeClose(){
        return true;
    }

    /**
     * <p>起動条件 : F1(閉じる)押下時</p>
     * <p>処理概要 : 後処理を実行する</p>
     * 
     * @return F1(閉じる)処理における後処理の成否
     */
    @Override
    protected boolean postClose(){
        return true;
    }

    /**
     * <p>起動条件 : F2(メニュー)押下時</p>
     * <p>処理概要 : 前処理を実行する</p>
     * 
     * @return F2(メニュー)処理における前処理の成否
     */
    @Override
    protected boolean preMenu(){
        return true;
    }

    /**
     * <p>起動条件 : F2(メニュー)押下時</p>
     * <p>処理概要 : 本処理を実行する</p>
     * 
     * @return F2(メニュー)処理における本処理の成否
     */
    @Override
    protected boolean executeMenu(){
        return true;
    }

    /**
     * <p>起動条件 : F2(メニュー)押下時</p>
     * <p>処理概要 : 後処理を実行する</p>
     * 
     * @return F2(メニュー)処理における後処理の成否
     */
    @Override
    protected boolean postMenu(){
        return true;
    }

    /**
     * <p>起動条件 : F3(クリア)押下時</p>
     * <p>処理概要 : 前処理を実行する</p>
     * 
     * @return F3(クリア)処理における前処理の成否
     */
    @Override
    protected boolean preClear(){
        return true;
    }

    /**
     * <p>起動条件 : F3(クリア)押下時</p>
     * <p>処理概要 : 本処理を実行する</p>
     * 
     * @return F3(クリア)処理における本処理の成否
     */
    @Override
    protected boolean executeClear(){
        return true;
    }

    /**
     * <p>起動条件 : F3(クリア)押下時</p>
     * <p>処理概要 : 後処理を実行する</p>
     * 
     * @return F3(クリア)処理における後処理の成否
     */
    @Override
    protected boolean postClear(){
        return true;
    }

    /**
     * <p>起動条件 : F4(選択)押下時</p>
     * <p>処理概要 : 前処理を実行する</p>
     * 
     * @return F4(選択)処理における前処理の成否
     */
    @Override
    protected boolean preSelect(){
        return true;
    }

    /**
     * <p>起動条件 : F4(選択)押下時</p>
     * <p>処理概要 : 本処理を実行する</p>
     * 
     * @return F4(選択)処理における本処理の成否
     */
    @Override
    protected boolean executeSelect(){
        return true;
    }

    /**
     * <p>起動条件 : F4(選択)押下時</p>
     * <p>処理概要 : 後処理を実行する</p>
     * 
     * @return F4(選択)処理における後処理の成否
     */
    @Override
    protected boolean postSelect(){
        return true;
    }

    /**
     * <p>起動条件 : F5(検索)押下時</p>
     * <p>処理概要 : 前処理を実行する</p>
     * 
     * @return F5(検索)処理における前処理の成否
     */
    @Override
    protected boolean preSearch(){
        return true;
    }

    /**
     * <p>起動条件 : F5(検索)押下時</p>
     * <p>処理概要 : 本処理を実行する</p>
     * 
     * @return F5(検索)処理における本処理の成否
     */
    @Override
    protected boolean executeSearch(){
        return true;
    }

    /**
     * <p>起動条件 : F5(検索)押下時</p>
     * <p>処理概要 : 後処理を実行する</p>
     * 
     * @return F5(検索)処理における後処理の成否
     */
    @Override
    protected boolean postSearch(){
        return true;
    }

    /**
     * <p>起動条件 : F6(未定義)押下時</p>
     * <p>処理概要 : 前処理を実行する</p>
     * 
     * @return F6(未定義)処理における前処理の成否
     */
    @Override
    protected boolean preF06(){
        return true;
    }

    /**
     * <p>起動条件 : F6(未定義)押下時</p>
     * <p>処理概要 : 本処理を実行する</p>
     * 
     * @return F6(未定義)処理における本処理の成否
     */
    @Override
    protected boolean executeF06(){
        return true;
    }

    /**
     * <p>起動条件 : F6(未定義)押下時</p>
     * <p>処理概要 : 後処理を実行する</p>
     * 
     * @return F6(未定義)処理における後処理の成否
     */
    @Override
    protected boolean postF06(){
        return true;
    }

    /**
     * <p>起動条件 : F7(未定義)押下時</p>
     * <p>処理概要 : 前処理を実行する</p>
     * 
     * @return F7(未定義)処理における前処理の成否
     */
    @Override
    protected boolean preF07(){
        return true;
    }

    /**
     * <p>起動条件 : F7(未定義)押下時</p>
     * <p>処理概要 : 本処理を実行する</p>
     * 
     * @return F7(未定義)処理における本処理の成否
     */
    @Override
    protected boolean executeF07(){
        return true;
    }

    /**
     * <p>起動条件 : F7(未定義)押下時</p>
     * <p>処理概要 : 後処理を実行する</p>
     * 
     * @return F7(未定義)処理における後処理の成否
     */
    @Override
    public boolean postF07(){
        return true;
    }

    /**
     * <p>起動条件 : F8(確定)押下時</p>
     * <p>処理概要 : 前処理を実行する</p>
     * 
     * @return F8(確定)処理における前処理の成否
     */
    @Override
    protected boolean preCommit(){
        return true;
    }

    /**
     * <p>起動条件 : F8(確定)押下時</p>
     * <p>処理概要 : 本処理を実行する</p>
     * 
     * @return F8(確定)処理における本処理の成否
     */
    @Override
    protected boolean executeCommit(){
        return true;
    }

    /**
     * <p>起動条件 : F8(確定)押下時</p>
     * <p>処理概要 : 後処理を実行する</p>
     * 
     * @return F8(確定)処理における後処理の成否
     */
    @Override
    protected boolean postCommit(){
        return true;
    }

}
