<p align="center">
 <img width="100px" src="https://res.cloudinary.com/anuraghazra/image/upload/v1594908242/logo_ccswme.svg" align="center" alt="GitHub Readme Stats" />
 <h2 align="center">UnitTest cơ bản với JUnit</h2>
</p>

Kiểm thử đơn vị rất quan trọng vì các nhà phát triển phần mềm đôi khi cố gắng tiết kiệm thời gian thực hiện kiểm thử đơn vị tối thiểu và điều này là sai lầm vì kiểm thử đơn vị không phù hợp dẫn đến chi phí cao Sửa lỗi trong quá trình Kiểm tra hệ thống , Kiểm tra tích hợp và thậm chí là Kiểm thử Beta sau khi ứng dụng được xây dựng. Nếu thử nghiệm đơn vị thích hợp được thực hiện trong quá trình phát triển sớm, thì cuối cùng nó sẽ tiết kiệm thời gian và tiền bạc.

<div align="center">
 <table >
  <theader>
  <th>
   Nội Dung 
   </th>
   </theader>
  <tbody>
  <td>
   1: Kiểm thử đơn vị là gì?</br>
   2: Thiết kế unit test?</br>
   3: các test cơ bản?</br>
   4: Thiết lập dự án</br>
   5: kết luận 
   </td>
   
   </tbody>
   </table>
</div>

# Kiểm thử đơn vị là gì?

- là một loại kiểm thử phần mềm trong đó các đơn vị hoặc thành phần riêng lẻ của phần mềm được kiểm tra. Mục đích là để xác nhận rằng mỗi đơn vị của mã phần mềm hoạt động như mong đợi. Kiểm thử đơn vị được thực hiện trong quá trình phát triển (giai đoạn mã hóa) của một ứng dụng bởi các nhà phát triển. Unit Test tách một phần mã và xác minh tính đúng đắn của nó. Một đơn vị có thể là một chức năng, phương pháp, thủ tục, mô-đun hoặc đối tượng riêng lẻ.

# Thiết kế unit test

> 1.Thiết lập các điều kiện cần thiết bao gồm khởi tạo các đối tượng, xác định tài nguyên cần thiết, xây dựng các dữ liệu giả.\
> 2.Gọi các phương thức cần kiểm tra.\
> 3.Kiểm tra sự hoạt động đúng đắn của các phương thức.\
> 4.Dọn dẹp tài nguyên sau khi kết thúc kiểm tra.

# các test cơ bản 

```
 int sum(int a, int b){
        if(a < 0 || b < 0){
            throw new IllegalArgumentException(" a > 0 va b > 0");
        }
        return a+ b;
    }

    @Test
    void checkSumTwoNumber(){
         @Test
    void checkSumTwoNumber(){
        Assertions.assertThrows(IllegalArgumentException.class, ()->{
            sum(-5,10);
        });
        Assertions.assertEquals(15,sum(5,10));
    }

    }
    
```

-Assertions.assertThrows() xử lí các ngoại lệ 
-Assertions.assertEquals() kiểm tra kết quả trả về 
xem thêm [tại đây](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/util/Assert.html)

# Thiết lập dự án Sping boot

## Maven Dependencies

```md
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter-api</artifactId>
            <version>5.9.0</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <scope>runtime</scope>
        </dependency>
```

## Tạo lớp sinh viên

```md
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table
@Entity
public class SinhVien {

    @Id
    private String id;
    private String name;
    private String email;

}

```

##Service

> service
```
public interface SinhVienService {

    List<SinhVien> getList();

    SinhVien addNew(SinhVien sinhVien);

    boolean delete(String id);

    SinhVien update(String id, SinhVien sinhVien);

    SinhVien findById(String id);
}

```

> impl
```
@Service
public class SinhVienSericeImpl implements SinhVienService{

    @Autowired
    private  SinhVienRepository sinhVienRepository;

    @Override
    public List<SinhVien> getList() {
        return sinhVienRepository.findAll();
    }

    @Override
    public SinhVien addNew(SinhVien sinhVien) {
        if(sinhVienRepository.selectExistsEmail(sinhVien.getEmail()) != 0){
            throw  new IllegalArgumentException("email da ton tai");
        }
        System.out.println(sinhVienRepository.selectExistsEmail(sinhVien.getEmail()));
        return sinhVienRepository.save(sinhVien);
    }

    @Override
    public boolean delete(String id) {
        Optional<SinhVien> sinhVien = sinhVienRepository.findById(id);
        if (sinhVien.isPresent()) {
            sinhVienRepository.deleteById(id);
            return true;
        }else{
            throw  new IllegalArgumentException("id not exists");
        }
    }

    @Override
    public SinhVien update(String id, SinhVien sinhVien) {
        SinhVien updateSinhVien = sinhVienRepository.findById(id).orElse(null);
        if(updateSinhVien == null){
            throw  new IllegalArgumentException("id not exists");
        }
        if(sinhVienRepository.selectExistsEmail(sinhVien.getEmail()) == 0){
            throw  new IllegalArgumentException("email da ton tai");
        }
        updateSinhVien.setId(sinhVien.getId());
        updateSinhVien.setName(sinhVien.getName());
        updateSinhVien.setEmail(sinhVien.getEmail());
        return sinhVienRepository.save(updateSinhVien);
    }

    @Override
    public SinhVien findById(String id) {
        return sinhVienRepository.findById(id).orElse(null);
    }
}

```

## Controller

```md
@RestController
public class sinhVienController {

    @Autowired()
    private SinhVienService sinhVienService;

    @PostMapping()
    public SinhVien addNew(@RequestBody SinhVien sinhVien){
        return sinhVienService.addNew(sinhVien);
    }

    @GetMapping("/{id}")
    public SinhVien DetailSinhVien(@PathVariable("id") String id){
        SinhVien sinhVien = null;
        try {
            sinhVien = sinhVienService.findById(id);
        }catch (Exception e){
            System.out.println(e);
        }
        return sinhVien;
    }

    @PutMapping("/{id}")
    public SinhVien update(@PathVariable("id") String id, @RequestBody SinhVien sinhVienRequest) {
        return sinhVienService.update(id, sinhVienRequest);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable("id") String id){
        return sinhVienService.delete(id);
    }

    @GetMapping()
    public List<SinhVien> listSinhVien(){
        List<SinhVien> list = sinhVienService.getList();
        return list;
    }

}
```

## test 
## Khởi tạo các test case 

```
@RunWith(MockitoJUnitRunner.class)
class sinhVienControllerTest {

    @Mock
    private SinhVienService sinhVienService ;
    
     @InjectMocks
    private SinhVienController sinhVienController ;
    
    @Autowired
    private MockMvc mvc;

   // khởi tạo sinh viên 
   SinhVien sinhVien = new SinhVien("A1","Le Van A", "leVanA@gmail.com");

    ObjectMapper objectMapper = new ObjectMapper();
    ObjectWriter objectWriter = objectMapper.writer();
    
    @Test
    void addNew() {
    }

    @Test
    void detailSinhVien() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }

    @Test
    void listSinhVien() {
    }

    @Test
    void testAddNew() {
    }

    @Test
    void detailSinhVien() {
    }

    @Test
    void testUpdate() {
    }

    @Test
    void testDelete() {
    }

    @Test
    void testListSinhVien() {
    }
}
```

-@RunWith (SpringRunner.class) cung cấp cầu nối giữa các tính năng kiểm tra Spring Boot và JUnit. Bất cứ khi nào chúng ta sử dụng bất kỳ tính năng kiểm tra Spring Boot nào trong các bài kiểm tra JUnit , chú thích này sẽ được yêu cầu.
-@InjectMocks tạo một thể hiện của lớp và đưa các mô phỏng được tạo bằng chú thích @Mock vào thể hiện này
-@Mock giúp là cách viết tắt của phương thức Mockito.mock () , giúp bạn bạn dễ dàng tìm thấy mô phỏng vấn đề trong trường hợp bị lỗi, vì tên của trường xuất hiện trong thông báo lỗi
-MockMvc là một lớp công cụ kiểm tra Spring Boot cho phép bạn kiểm tra các test case mà không cần khởi động máy chủ HTTP
-AutoCloseable là một interface đảm bảo rằng các tài nguyên được tự động giải phóng

> thực hiện cấu hình test
```
   @BeforeEach
    public void setup() {
        autoCloseable =  MockitoAnnotations.openMocks(this);
        this.mvc = MockMvcBuilders.standaloneSetup(sinhVienController).build();
    }
    
    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }
```

-@BeforeEach  thực thi trước mỗi lần gọi phương thức @Test
-@AfterEach sẽ được thực thi sau mỗi  phương thức trong lớp hiện tại.@Test
-MockitoAnnotations.openMocks(this) gọi yêu cầu Mockito quét cá thể lớp thử nghiệm này để tìm bất kỳ trường nào được chú thích bằng @Mockvà khởi tạo các trường đó dưới dạng mô phỏng
-MockMvcBuilders.standaloneSetup() cho phép đăng ký một hoặc nhiều controller mà không cần sử dụng WebApplicationContext đầy đủ

### 1 cung cấp danh sách sinh viên  sau đó trả lại Mảng Json

```

@Test
    void givenStudents_whenGetStudents_thenReturnJsonArray() throws Exception {
        List<SinhVien> listSinhVien = new ArrayList<>(Arrays.asList(sinhVien));
        Mockito.when(sinhVienService.getList()).thenReturn(listSinhVien);

        mvc.perform(get("/list-sinh-vien")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(sinhVien.getName())));
    }

```
-mvc.perform() trả về một kiểu cho phép xâu chuỗi các hành động tiếp theo, chẳng hạn như xác nhận các kỳ vọng, vào kết quả.
-andExpect thực hiện các test case 
-Lời gọi phương thức get (…) có thể được thay thế bằng các phương thức khác tương ứng với các động từ HTTP như put () , post () , v.v. Xin lưu ý rằng chúng tôi cũng đang đặt loại nội dung trong yêu cầu.

#### 2 thêm mới sinh viên 


```
 @Test
    void whenPostStudents_thenCreateStudent() throws Exception {
        SinhVien sinhVien1 = SinhVien.builder()
                .id("A2")
                .name("Le van b")
                .email("b@gmail.com")
                .build();

        Mockito.when(sinhVienService.addNew(sinhVien1)).thenReturn(sinhVien1);

        String content = objectWriter.writeValueAsString(sinhVien1);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.post("/add")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content);

        mvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is("Le van b")));
    }
```
-objectWriter.writeValueAsString chuyển đổi chuỗi về ía trị json

#### test tìm kiếm sinh viên theo tên :

```
 @Test
    void detailSinhVien() throws Exception {
        Mockito.when(sinhVienService.findById(sinhVien.getId())).thenReturn(sinhVien);

        mvc.perform(get("/A1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is("Le Van A")));

    }
    
```

#### test update sinh viên

```
 @Test
    void updateSinhVien() throws Exception {
        SinhVien updateSinhVien = SinhVien.builder()
                .id("A1")
                .name("Le van b")
                .email("b@gmail.com")
                .build();
        SinhVien editSinhVien = new SinhVien("A1","Le Van B", email);
        Mockito.when(sinhVienService.update(editSinhVien.getId(), editSinhVien)).thenReturn(updateSinhVien);
        String content = objectWriter.writeValueAsString(updateSinhVien);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.put("/"+sinhVien.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content);
        mvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status().isOk());

    }
    
```

# kết luận 
Trong bài viết này, chúng ta đã học cách xây dựng UnitTest cơ bản với JUnit 

tất cả các mẫu mã hiển thị trong bài viết đều có sẵn [trên github](https://github.com/thangdtph27626/unitTest.github.io)
