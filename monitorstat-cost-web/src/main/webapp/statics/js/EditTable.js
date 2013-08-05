var tableEdit = {
    readyCall:EditTable
};

//���ÿɱ༭�ı��
//�������ԣ�Ҫѡ���tdԪ�أ�tdԪ���ϵ��������ԣ��ص�����
function EditTable(selectElements,myProp,onEditDone){
	var tds=$(selectElements);
    //�����е�td��ӵ���¼�
    tds.dblclick(function(){
        //��õ�ǰ����Ķ���
        var td=$(this);
        //ȡ����ǰtd���ı����ݱ�������
        var oldText=td.text();
        //����һ���ı��������ı����ֵΪ�����ֵ   
        var input=$("<input type='text' value='"+oldText+"'/>"); 
        //����ǰtd������������Ϊinput
        td.html(input);
        //�����ı���ĵ���¼�ʧЧ
        input.click(function(){
            return false;
        });
        input.dblclick(function(){
            return false;
        });
        //�����ı������ʽ
        input.css("border-width","0");              
        input.css("font-size","16px");
        input.css("text-align","center");
        //�����ı����ȵ���td�Ŀ��
        input.width(td.width());
        //���ı���õ�����ʱ����ȫѡ�¼�  
        input.trigger("focus").trigger("select"); 
        //���ı���ʧȥ����ʱ���±�Ϊ�ı�
        input.blur(function(){
            var input_blur=$(this);
            //���浱ǰ�ı��������
            var newText=input_blur.val(); 
            td.html(newText); 

			conpareValue(td,myProp,oldText,onEditDone);
        }); 
        //��Ӧ�����¼�
        input.keyup(function(event){
            // ��ȡ��ֵ
            var keyEvent=event || window.event;
            var key=keyEvent.keyCode;
            //��õ�ǰ����
            var input_blur=$(this);
            switch(key)
            {
                case 13://���»س��������浱ǰ�ı��������
                    var newText=input_blur.val(); 
                    td.html(newText); 
					conpareValue(td,myProp,oldText,onEditDone);
                break;
                
                case 27://����esc����ȡ���޸ģ����ı������ı�
                    td.html(oldText); 
                break;
            }
        });
        
       
        
    });
}

function conpareValue(td,myProp,oldV,onEditDone){
	if(td.text().trim()==""){
		td.html(oldV); 
	}

	if(td.text().trim()!=oldV){
					
		onEditDone(td,oldV,td.attr(myProp));
	}
}
